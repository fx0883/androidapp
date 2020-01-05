package com.colorgarden.app6.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.colorgarden.app6.R;
import com.colorgarden.app6.adapter.AdapterDialog;
import com.colorgarden.app6.adapter.AdapterSubColor;
import com.colorgarden.app6.colorManager.ModelColor;
import com.colorgarden.app6.colorManager.ModelGetColors;
import com.colorgarden.app6.constant.Constant;
import com.colorgarden.app6.utils.ConnectionDetector;
import com.colorgarden.app6.utils.FillTaskParam;
import com.colorgarden.app6.utils.ModelImagePoint;
import com.colorgarden.app6.utils.NoInternetDialog;
import com.colorgarden.app6.utils.OfflineData;
import com.colorgarden.app6.utils.TheTask;
import com.colorgarden.app6.view.TouchImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.colorgarden.app6.constant.Constant.DEFAULT_COLOR;
import static com.colorgarden.app6.constant.Constant.SAVED_WORK_PATH;
import static com.colorgarden.app6.constant.Constant.UPLOAD_URL;
import static com.colorgarden.app6.constant.Constant.getSavedImgPath;
import static com.colorgarden.app6.constant.Constant.workImgSavedOrNot;


public class PaintActivity extends AppCompatActivity implements AdapterSubColor.ClickInterface, NoInternetDialog.wifiOninterface {


    public static int replace_color, fill_color;
    public static String selected_color;
    public static Bitmap bitmap;
    public static Bitmap originalBitmap = null;
    public static Bitmap currentBitmap = null;
    public static List < ModelImagePoint > undo_list;
    public static List < ModelImagePoint > redo_list;
    private static int currentX;
    private static int currentY;
    public TouchImageView imageDraw;
    public Drawable place_holder;
    @BindView(R.id.btn_close)
    TextView btn_close;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rec_dialog)
    RecyclerView rec_dialog;
    @BindView(R.id.pager_color)
    ViewPager pager_color;
    @BindView(R.id.layout_dialog)
    RelativeLayout layout_dialog;
    @BindView(R.id.btn_brush)
    ImageView btn_brush;
    @BindView(R.id.btn_colorpicker)
    ImageView btn_color_picker;
    @BindView(R.id.back)
    ImageView back;

    int get_color;
    boolean isSave;
    String get_pos;
    RecyclerView rec_2;
    ColorPagerAdapter colorPagerAdapter;
    AdapterSubColor pagerSubRec;
    AdapterDialog dialogRecAdapter;
    Point point;
    Dialog dialog;
    HSLColorPicker dialog_color_picker;
    Button dialog_btn_cancel, dialog_btn_ok;
    String image_path;
    List < ModelColor > colors_list = new ArrayList <>();

    boolean isEdit = false;
    ConnectionDetector cd;
    ProgressDialog p_dialog;
    TheTask theTask;
    ProgressBar progress;
    NoInternetDialog noInternetDialog = null;

    byte[] get_byte_arr;
    private Handler mHandler;
    private Boolean isCanClick = true;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        overridePendingTransition(R.anim.animate_card_enter, R.anim.animate_card_exit);

        setContentView(R.layout.activity_paint);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 0 :
                        isCanClick = false;
                        break;
                    case 1 :
                        isCanClick = true;
                        break;
                }
            }
        };




        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ButterKnife.bind(this);

        cd = new ConnectionDetector(this);
        selected_color = "null";
        get_pos = getIntent().getStringExtra("pos");
        place_holder = getResources().getDrawable(R.drawable.please_wait);
        originalBitmap = null;
        if (getIntent().getStringExtra("imgPath") != null) {
            isEdit = true;
            image_path = getIntent().getStringExtra("imgPath");
        } else {
            try {
                if (getIntent().getByteArrayExtra("bmp") != null) {
                    get_byte_arr = getIntent().getByteArrayExtra("bmp");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(get_byte_arr, 0, get_byte_arr.length);
                    place_holder = new BitmapDrawable(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
                place_holder = getResources().getDrawable(R.drawable.please_wait);

            }

        }
//        else {
//            Is_Home = getIntent().getBooleanExtra(IS_HOME, true);
//        }
//
//
//        if (!Is_Home) {
//            CAT_ID = getIntent().getStringExtra("cat_id");
//            CAT_NAME = getIntent().getStringExtra("cat_name");
//        }
        undo_list = new ArrayList <>();
        redo_list = new ArrayList <>();
        p_dialog = new ProgressDialog(PaintActivity.this);
        fill_color = Color.parseColor(DEFAULT_COLOR);
        selected_color = DEFAULT_COLOR;
//        selected_color = "#F9BFD5";
        replace_color = Color.WHITE;

        init();
        setBitmaps();
        clickListener();
        dialog = new Dialog(PaintActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        setAdapters();
        AdapterSubColor.setInterface(this);

    }


    public boolean isNullImageView() {
        return imageDraw.getDrawable() != null;
    }

    public void setBitmaps() {
        if (workImgSavedOrNot(image_path, SAVED_WORK_PATH)) {
            originalBitmap = BitmapFactory.decodeFile(image_path);
            imageDraw.setImageBitmap(originalBitmap);
            currentBitmap = originalBitmap.copy(originalBitmap.getConfig(), true);
//            theTask = new TheTask(currentBitmap, imageDraw);
            theTask = new TheTask(currentBitmap, imageDraw,mHandler);
            theTask.setPaintColor(fill_color);
        } else if (OfflineData.isOffline(getApplicationContext())) {
            File file = new File(getSavedImgPath(getApplicationContext()), get_pos);
            Log.e("path===", "" + file.getAbsolutePath());
            originalBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageDraw.setImageBitmap(originalBitmap);
            currentBitmap = originalBitmap.copy(originalBitmap.getConfig(), true);
//            theTask = new TheTask(currentBitmap, imageDraw);
            theTask = new TheTask(currentBitmap, imageDraw,mHandler);
            theTask.setPaintColor(fill_color);
            originalBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        } else {
            String path = UPLOAD_URL + get_pos;
            Log.e("path===", "" + path);
            progress.setVisibility(View.VISIBLE);

            Glide.with(getApplicationContext())
                    .load(path)
                    .placeholder(place_holder)
                    .error(place_holder)
                    .listener(new RequestListener < Drawable >() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target < Drawable > target, boolean isFirstResource) {
                            // log exception
                            Log.e("TAG", "Error loading image", e);
                            if (!OfflineData.isOffline(getApplicationContext()) && !cd.isConnectingToInternet()) {
                                originalBitmap = null;
                                noInternetDialog = new NoInternetDialog.Builder(PaintActivity.this).build();
                                noInternetDialog.seDialogListeners(PaintActivity.this);
                                Objects.requireNonNull(noInternetDialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_animation_fade;
                                noInternetDialog.show();
                            }
                            return false; // important to return false so the error placeholder can be placed
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target < Drawable > target, DataSource dataSource, boolean isFirstResource) {
                            originalBitmap = ((BitmapDrawable) resource).getBitmap();
                            Log.e("res===", "" + originalBitmap);
                            currentBitmap = originalBitmap.copy(originalBitmap.getConfig(), true);
                            Log.e("success_bitmap===", "" + currentBitmap.getWidth() + "==" + currentBitmap.getHeight());
//                            theTask = new TheTask(currentBitmap, imageDraw);
                            theTask = new TheTask(currentBitmap, imageDraw,mHandler);
                            theTask.setPaintColor(fill_color);
                            progress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageDraw);
        }

    }


//
//    Menu curMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.paint_menu, menu);
//        curMenu = menu;

//        menuItemUndo=mMenu.findItem(R.id.iv_undo);

        return  super.onCreateOptionsMenu(menu);
    }


    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem registrar = menu.findItem(R.id.iv_recreate);
        if (isEdit) {
            registrar.setVisible(false);
        } else {
            registrar.setVisible(true);
        }


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!this.isCanClick){
            return super.onOptionsItemSelected(item);
        }
        int id = item.getItemId();
        switch (id) {
            case R.id.iv_done:
                if (isNullImageView()) {
                    SaveImage();
                    selected_color = "null";
                    Intent intent = new Intent(getApplicationContext(), CreationActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_recreate:
                if (isNullImageView()) {
                    recreateActivity();
                }
                break;
            case R.id.iv_undo:
                if (isNullImageView()) {
                    FillTaskParam param = new FillTaskParam();
                    param.fillOrUndo = false;
                    theTask.execute(param);
//                    theTask.undo();
                    imageDraw.setImageBitmap(theTask.getbitmap());
                }
                break;
            case R.id.iv_redo:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void clickListener() {

        btn_color_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNullImageView()) {
                    layout_dialog.setVisibility(View.VISIBLE);
                }

            }
        });
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_dialog.setVisibility(View.GONE);
            }
        });

        btn_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNullImageView()) {
                    setColorDialog();
                }
            }
        });


        imageDraw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (imageDraw.getDrawable() != null) {
                    if (originalBitmap != null) {

                        imageDraw.setEnabled(false);
                        currentX = (int) event.getX();
                        currentY = (int) event.getY();
                        float devVsImgRatio = TouchImageView.drawableWidthForDeviceRelated / originalBitmap.getWidth();
                        PointF point = imageDraw.transformCoordTouchToBitmap(event.getX(), event.getY(), true);
                        currentX = (int) (point.x / devVsImgRatio);
                        currentY = (int) (point.y / devVsImgRatio);
                        bitmap = currentBitmap;
                        Point point1 = new Point();
                        point1.x = currentX;
                        point1.y = currentY;

                        FillTaskParam param = new FillTaskParam();
                        param.point = point1;
                        theTask.execute(param);
//                        theTask.floodFill(point1);
                        imageDraw.setImageBitmap(theTask.getbitmap());
                    }
                }

                return true;
            }
        });


    }

    private void setAdapters() {
        colors_list.clear();
        colors_list = ModelGetColors.getallcolordata();
        colorPagerAdapter = new ColorPagerAdapter(colors_list, getApplicationContext());
        pager_color.setAdapter(colorPagerAdapter);
        dialogRecAdapter = new AdapterDialog(getApplicationContext(), colors_list, new AdapterDialog.ClickInterface() {
            @Override
            public void recItemClick(View view, int i) {
                pager_color.setCurrentItem(i);
                layout_dialog.setVisibility(View.GONE);
            }
        });
        rec_dialog.setAdapter(dialogRecAdapter);
        colorPagerAdapter.notifyDataSetChanged();
        dialogRecAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (cd.isConnectingToInternet()) {
//            if (alertDialog != null && alertDialog.isShowing()) {
//                alertDialog.dismiss();
//            }
//        }
        setAdapters();
        Log.e("bitmap===", "" + originalBitmap);
//        if (!OfflineData.isOffline(getApplicationContext()) && originalBitmap == null) {
//            setBitmaps();
//        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("result_code===", "" + resultCode);
//        if (re)
        if (originalBitmap == null) {
            setBitmaps();
        }
    }

    private void init() {
        point = new Point();
        imageDraw = findViewById(R.id.image_draw);
        progress = findViewById(R.id.progress);
//        progress.getIndeterminateDrawable().setColorFilter(getResources()
//                .getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_white_back));
        rec_dialog.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        imageDraw.setMaxZoom(13);
//        Log.e("saved==", "img==" + get_pos + "save" + getSavedImgPath(getApplicationContext()) + imgSavedOrNot(get_pos, getSavedCatImgPath(getApplicationContext())));
        Log.e("saved_work==", "img==" + image_path + "save" + SAVED_WORK_PATH + workImgSavedOrNot(image_path, SAVED_WORK_PATH));
    }

    public void recreateActivity() {
        try {
            Intent intent = new Intent(getApplicationContext(), PaintActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            if (image_path != null) {
                intent.putExtra("imgpath", image_path);
            } else {
                intent.putExtra("pos", get_pos);
                intent.putExtra("bmp", get_byte_arr);
            }
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setColorDialog() {
        dialog.setContentView(R.layout.activity_colorpicker_dialog);
        dialog_color_picker = dialog.findViewById(R.id.dialog_color_picker);
        dialog_btn_cancel = dialog.findViewById(R.id.dialog_btn_cancel);
        dialog_btn_ok = dialog.findViewById(R.id.dialog_btn_ok);
        get_color = -12865103;
        dialog_color_picker.setColor(get_color);
        dialog_color_picker.setColorSelectionListener(new SimpleColorSelectionListener() {
            @Override
            public void onColorSelected(int color) {
                get_color = color;
            }
        });
        dialog_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_color = String.valueOf(get_color);
                fill_color = get_color;
                theTask.setPaintColor(fill_color);
                pager_color.setAdapter(colorPagerAdapter);
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    // Save Image
    private void SaveImage() {

        imageDraw.resetZoom();
        isSave = true;
        File dir = new File(Constant.SAVED_WORK_PATH);
        if (!dir.exists()) {
            boolean b = dir.mkdirs();
            Log.e("mkdir====", "" + b);
        }
        Bitmap res = ((BitmapDrawable) imageDraw.getDrawable()).getBitmap();


        File saveFile1;


        if (image_path != null) {
            saveFile1 = new File(image_path);
        } else {
            saveFile1 = new File(dir, System.currentTimeMillis() + ".jpg");
        }
        image_path = String.valueOf(saveFile1);
        if (saveFile1.exists()) {
            boolean b = saveFile1.delete();
            Log.e("delete===", "" + b);

        }

        try {

            FileOutputStream out = new FileOutputStream(saveFile1);
            res.compress(Bitmap.CompressFormat.PNG, 100, out);

            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.parse("file://"
                    + Environment.getExternalStorageDirectory());
            mediaScanIntent.setData(contentUri);
            (PaintActivity.this).sendBroadcast(mediaScanIntent);
        } else {
            (PaintActivity.this).sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
                    + Environment.getExternalStorageDirectory())));
        }
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(saveFile1.getAbsolutePath()))));

        Toast.makeText(this, R.string.msg_paint_saved, Toast.LENGTH_SHORT).show();

    }

    // Item Click
    @Override
    public void ItemClick(View view, int i, String s, int pagerPosition) {
        if (isNullImageView()) {
            selected_color = s;
            fill_color = Color.parseColor(selected_color);
            theTask.setPaintColor(fill_color);
            pager_color.setAdapter(colorPagerAdapter);
            pager_color.setCurrentItem(pagerPosition);
        }
    }

    @Override
    public void onBackPressed() {
        if (noInternetDialog != null) {
            noInternetDialog.onDestroy();
        }
//        originalBitmap = null;
//        currentBitmap = null;
        if (!isSave) {
            showExitDialog();
        } else {
            finish();
        }
    }

    // Exit Page Dialog
    public void showExitDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(PaintActivity.this, R.style.MyDialogTheme);
        builder.setMessage(getString(R.string.lbl_paint_quitWithoutSaving));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.btn_main_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setMainIntent();
            }
        });
        builder.setNegativeButton(R.string.btn_main_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    // Pass Intent
    public void setMainIntent() {
        selected_color = "null";
        if (!isEdit) {
            finish();
//            Intent intent=new Intent(getApplicationContext(),ActivityImageCategory.class);
//            startActivity(intent);
//            finish();
        } else {
            sendIntent();
        }
    }

    private void sendIntent() {
        Intent intent = new Intent(getApplicationContext(), CreationActivity.class);
        startActivity(intent);
        finish();
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        checkOrientation(newConfig);
//
//    }

//    private void checkOrientation(Configuration newConfig) {
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Log.e("orientation===", "landscape");
////            mAdView.setVisibility(View.GONE);
////            layout_ad.setVisibility(View.GONE);
////            layout_space.setVisibility(View.GONE);
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Log.e("orientation===", "portrait");
////            mAdView.setVisibility(View.VISIBLE);
////            layout_ad.setVisibility(View.VISIBLE);
////            layout_space.setVisibility(View.VISIBLE);
//        }
//    }


    @Override
    public void wifiEnabled() {
        Log.e("wifi_enabled====", "" + originalBitmap);
        if (originalBitmap == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("run===", "true");
                    setBitmaps();
                }
            }, 1000);

        }
    }

    // Color Adapter
    public class ColorPagerAdapter extends PagerAdapter {
        List < ModelColor > color_utils_list;
        Context context;

        ColorPagerAdapter(List < ModelColor > color_utils_list, Context context) {
            this.color_utils_list = color_utils_list;
            this.context = context;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_pagercolor, container, false);
            TextView tv_color_name = view.findViewById(R.id.tv_color_name);
            rec_2 = view.findViewById(R.id.rec_2);
            tv_color_name.setText(color_utils_list.get(position).nameResId);
            pagerSubRec = new AdapterSubColor(getApplicationContext(), color_utils_list.get(position).getSetColors(), position);
            rec_2.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            rec_2.setAdapter(pagerSubRec);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return color_utils_list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

    }

}