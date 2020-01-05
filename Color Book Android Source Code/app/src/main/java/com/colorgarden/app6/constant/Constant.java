package com.colorgarden.app6.constant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.colorgarden.app6.R;
import com.colorgarden.app6.activity.ActivityImageCategory;
import com.colorgarden.app6.utils.SharedPreferencesUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;
import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;


public class Constant {
//    public static String BASE_URL = "http://192.168.0.100/colorbook/api/";
//    //    public static String BASE_URL = "http://192.168.0.107/colorbook/api/";
//    public static String UPLOAD_URL = "http://192.168.0.100/colorbook/uploads/";

//    public static String BASE_URL = "http://templatevilla.net/codecanyon/colorbookadmin/admin/api/";
//
//    public static String UPLOAD_URL =  "http://templatevilla.net/codecanyon/colorbookadmin/admin/uploads/";

//    public static String BASE_URL = "http://111.231.224.94/api/";
//
//    public static String UPLOAD_URL =  "http://111.231.224.94/uploads/";

    public static String BASE_URL = "http://47.101.48.98/api/";

    public static String UPLOAD_URL =  "http://47.101.48.98/uploads/";
    public static String THUMB_URL = UPLOAD_URL + "thumbs/";
    public static String SAVED_WORK_PATH = Environment.getExternalStorageDirectory() + "/ColorBook/Work/";
//public static String SAVED_WORK_PATH = Environment.getExternalStorageDirectory() + "/GDTDOWNLOAD/";

    public static String DEFAULT_COLOR = "#F02640";
    public static int HOME_IMG_SIZE = 6;
    public static int PAGINATION_SIZE = 10;
    public static String MY_PREFS = "my_prefs";
    public static String COLOR = "set_color";

    public static String getSavedImgPath(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/ColorBookMain/";
    }

    public static String getSavedCatImgPath(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/ColorBookCat/";
    }

    public static void loadBaseUrlOnline(final Context context){
        String configUrl = "http://fx0883.github.io/MySite/colorgarden.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(GET, configUrl, null, new Response.Listener <JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("res====", "" + response.toString());
                try {
                    boolean isUpdate = response.getBoolean("needupdate");
                    if(isUpdate){
                        String baseUrl = response.getString("baseurl");
                        Constant.BASE_URL = baseUrl+"api/";
                        Constant.UPLOAD_URL = baseUrl+"uploads/";
                        THUMB_URL = UPLOAD_URL + "thumbs/";
                    }

                    boolean isFirst = response.getBoolean("bisfirst");
                    SharedPreferencesUtil.getInstance(context).putSPBool("bisfirst",isFirst);
                }
                catch (Exception e){
                    Log.e("error====", "" + e.getMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }


    public static void saveImages(String names, String path, Bitmap bitmap) {
        try {
            File myDir = new File(path);
            if (!myDir.exists()) {
                boolean b = myDir.mkdirs();
                Log.e("create==", "" + b);
            }
            myDir = new File(myDir, names);
            FileOutputStream out = new FileOutputStream(myDir);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            out.flush();
            out.close();
        } catch (Exception ignored) {
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean imgSavedOrNot(String img_name, String path) {
        if (img_name != null) {
            File dir = new File(path);
            if (dir.exists()) {
                File[] files = dir.listFiles();
                for (File file : files) {
                    if (img_name.equals(file.getName())) {
                        Log.e("match==", "true");
                        return true;
                    }
                }
            }
        }
        return false;

    }


    public static boolean workImgSavedOrNot(String img_name, String path) {
        if (img_name != null) {
            File dir = new File(path);
            if (dir.exists()) {
                File[] files = dir.listFiles();
                for (File file : files) {
                    Log.e("same==", "" + img_name + "==" + file.getAbsolutePath());

                    if (img_name.equals(file.getAbsolutePath())) {
                        Log.e("match==", "true");
                        return true;
                    }
                }
            }
        }
        return false;

    }


    public static List<String> getAllCreationList() {
        List<String> fileList = new ArrayList<>();
        File dir = new File(SAVED_WORK_PATH);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                fileList.add(file.getAbsolutePath());
            }
            return fileList;
        }
        return fileList;
    }

    public static void shareImg(String path, Context context) {
        File saveFile = new File(path);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        Uri uri = null;

        if (Build.VERSION.SDK_INT >= M) {
            try {
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", saveFile);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

            }


        } else {
            uri = Uri.fromFile(saveFile);
        }
        intent.putExtra(Intent.EXTRA_TEXT, context.getApplicationContext().getPackageName());
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        String shareMsg = context.getString(R.string.share_message);
//        intent.putExtra(Intent.EXTRA_TEXT, "Hey look, I have created an awesome painting!!!");
        intent.putExtra(Intent.EXTRA_TEXT, shareMsg);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Intent chooserIntent = Intent.createChooser(intent, "Share Image");
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(chooserIntent);
    }


}
