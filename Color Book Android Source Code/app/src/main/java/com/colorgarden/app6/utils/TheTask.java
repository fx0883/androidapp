package com.colorgarden.app6.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;


public class TheTask {

    private final Bitmap bmp;
    private Bitmap bmpCopy = null;
    @SuppressLint("StaticFieldLeak")
    private
    ImageView imageV;
    private Point pt;
    private int replacementColor;
    private List<ModelImagePoint> undo_list = new ArrayList<>();
    private List<ModelImagePoint> redo_list = new ArrayList<>();
    private int targetColor;
    private Paint paint;

    Handler mHandler;

    public TheTask(Bitmap bmp, ImageView imageView) {
        this.paint = new Paint();
        this.bmp = bmp;
        this.paint.setColor(-1);
        this.imageV = imageView;
    }


    public TheTask(Bitmap bmp, ImageView imageView, Handler handler) {
        this.paint = new Paint();
        this.bmp = bmp;
        this.paint.setColor(-1);
        this.imageV = imageView;
        this.mHandler = handler;
    }

    private void floodFill(Point point) {
        if (point.y < bmp.getHeight()) {
            if (point.x < bmp.getWidth()) {
                int pixel = this.bmp.getPixel(point.x, point.y);
                fillColorAbsolute(point);
                undo_list.add(new ModelImagePoint(pixel, point));
            }
        }
    }

    private void fillColorAbsolute(Point point) {
        int i = this.bmp.getPixel(point.x, point.y);
        int color = this.paint.getColor();
        if (i > Color.parseColor("#000000")) {
            bmpCopy = bmp.copy(bmp.getConfig(), true);
            this.floodFill(bmpCopy, point, i, color);
        }
    }

    public Bitmap getbitmap() {
        return this.bmp;
    }

    public void setPaintColor(int color) {
        int parseColor = Color.parseColor("#000000");
        if (color <= parseColor) {
            color = parseColor + 1;
        }
        this.paint.setColor(color);
    }

    private void undo() {
        if (undo_list.size() > 0) {
            ModelImagePoint modelUndolist = undo_list.get(undo_list.size() - 1);
            int color = this.paint.getColor();
            this.paint.setColor(modelUndolist.color);
            fillColorAbsolute(modelUndolist.point);
            this.paint.setColor(color);
            redo_list.add(undo_list.get(undo_list.size() - 1));
            undo_list.remove(undo_list.size() - 1);
        }
    }





    //判断是不是边界
    private boolean isBlack(int i, int j) {
        if (Color.red(i) == Color.green(i) && Color.green(i) == Color.blue(i) && Color.red(i) < 150 || i == j) {
            return true;
        }
        return false;
//        if (Color.red(i) == Color.green(i) && Color.green(i) == Color.blue(i) && Color.red(i) < 150) {
//            return true;
//        }
//        return false;
    }


    /***************************************************************************/
    /**
     * 扫描线算法
     *
     * @author chicken
     *
     */
    public void floodFill(Bitmap bitmap, Point point, int oldColor, int newColor) {

        int[] pixels;
        Stack<Point> pointStack = new Stack<>();
        int width, height;

        /**
         * 处理耗时操作
         */
        // 算法初始化
        pointStack.clear();// 清空源粒子栈
        pointStack.push(point);// 入栈

        width = bitmap.getWidth();
        height = bitmap.getHeight();
        pixels=new int[width*height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);


//        MainActivity.enterTime = System.currentTimeMillis();
        /**************************************************/
        Point tmp;
        int x,y,XLeft,XRight,index;
        while (!pointStack.isEmpty()) {
            tmp = pointStack.pop();
            x = tmp.x;
            y = tmp.y;
            XLeft = XRight = x;
//            while (x > 0 && (curColor=pixels[index=width*y+x]) == oldColor
//                    && curColor != fillColor)
            while (x > 0 && !isBlack(pixels[index=width*y+x], newColor))
            {
                bitmap.setPixel(x, y, newColor);
                pixels[index]=newColor;
                x--;
            }
            XLeft=x+1;

            x = tmp.x + 1;
            while (x < width && !isBlack(pixels[index=width*y+x], newColor)) {
                bitmap.setPixel(x, y, newColor);
                pixels[index]=newColor;
                x++;
            }
            XRight=x-1;

            if (y > 0) {
                findNewSeedInline(pointStack,pixels,width, XLeft, XRight, y - 1, newColor);
            }
            if (y + 1 < height) {
                findNewSeedInline(pointStack,pixels,width, XLeft, XRight, y + 1, newColor);
            }
        }
        /**************************************************/
//        MainActivity.exitTime = System.currentTimeMillis();
//        MainActivity.takedTime = MainActivity.exitTime - MainActivity.enterTime;
//        (MainActivity.timeTxt).setText(Long.toString(MainActivity.takedTime));
    }



    public void findNewSeedInline(Stack<Point> pointStack, int[] pixels, int width, int XLeft, int XRight, int y, int newColor) {
        Point p;
        Boolean pflag;
//        int x = XLeft + 1;
        int x = XLeft;
        while (x <= XRight) {
            pflag = false;

            while (x < XRight
                    && !isBlack(pixels[width*y+x], newColor)) {

                if (pflag == false) {
                    pflag = true;
                }
                x++;
            }
            if (pflag == true) {
                if ((x == XRight) && !isBlack(pixels[width*y+x], newColor)) {
                    p = new Point(x, y);
                    pointStack.push(p);
                } else {
                    p = new Point(x - 1, y);
                    pointStack.push(p);
                }
                pflag = false;
            }

            // 处理向右跳过内部的无效点（处理区间右端有障碍点的情况）
            int xenter = x;
            while (isBlack(pixels[width*y+x], newColor)) {
                if (x >= XRight || x >= width) {
                    break;
                }
                x++;
            }
            if (xenter == x) {
                x++;
            }
        }
    }



    public Void execute(FillTaskParam param) {
        new InnerTask().execute(param);
        return null;
    }

    private InnerTask interTask = null;
    public class InnerTask extends AsyncTask<FillTaskParam, Integer, Void> {

        @Override
        protected Void doInBackground(FillTaskParam... fillTaskParams) {
            interTask = this;
//            imageV.setEnabled(false);
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessage(msg);//用activity中的handler发送消息



            FillTaskParam param = fillTaskParams[0];
            if (param.fillOrUndo) {
                if (param.point != null) {
//                    publishProgress(1);
                    floodFill(param.point);
//                    publishProgress(1);
                }
            } else {
                undo();
            }

            return null;
        }

        public void pulishProgressEx(Integer... values){
            publishProgress(values);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
//            if((values[0] % 20) == 00){
                if (bmpCopy != null) {
                    Canvas canvas = new Canvas(bmp);
                    canvas.drawBitmap(bmpCopy, new Matrix(), null);
                    imageV.setImageBitmap(bmp);
//                    imageV.setEnabled(true);
                }
//            }

        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (bmpCopy != null) {
                Canvas canvas = new Canvas(bmp);
                canvas.drawBitmap(bmpCopy, new Matrix(), null);
                imageV.setImageBitmap(bmp);
                imageV.setEnabled(true);

                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);//用activity中的handler发送消息

            }
        }
    }
}
