package com.ChildHealthDiet.app2.constant;

import android.content.Context;
import android.os.Environment;

import android.util.Log;


import java.io.File;
import java.util.ArrayList;
import java.util.List;




public class Constant {

    public static Boolean DEBUG = true;
    public static String BASE_IMAGE_URL = "http://39.96.168.2:8090/images";
    public static String BASE_URL = "http://39.96.168.2:8090";
    public static String UPLOAD_URL =  "http://39.96.168.2/uploads/";
//    public static String BASE_URL_USER = "http://192.168.31.206:8000";
////    public static String BASE_URL_USER = "http://localhost.charlesproxy.com:8000";
//    public static String BASE_URL_USER_AVATAR = "http://192.168.31.206:8000/upload/avatar/";
//    public static String BASE_URL_UPLOAD_IMAGES = "http://192.168.31.206:8000/upload/color_images/";

    public static String BASE_URL_USER = "http://47.101.48.98/";
    //    public static String BASE_URL_USER = "http://localhost.charlesproxy.com:8000";
    public static String BASE_URL_USER_AVATAR = "http://47.101.48.98/upload/avatar/";
    public static String BASE_URL_UPLOAD_IMAGES = "http://47.101.48.98/upload/color_images/";



    public static int APP_ID = 1;



//    public static String BASE_IMAGE_URL = "http://47.101.48.98/api/";
//
//    public static String UPLOAD_URL =  "http://47.101.48.98/uploads/";
    public static String THUMB_URL = UPLOAD_URL + "thumbs/";
    public static String SAVED_WORK_PATH = Environment.getExternalStorageDirectory() + "/ColorBook/Work/";
//public static String SAVED_WORK_PATH = Environment.getExternalStorageDirectory() + "/GDTDOWNLOAD/";

    public static String DEFAULT_COLOR = "#F02640";
    public static int HOME_IMG_SIZE = 6;
    public static int PAGINATION_SIZE = 10;
    public static String MY_PREFS = "my_prefs";
    public static String COLOR = "set_color";



    public static String SERVICE_AGREEMENT_URL = "http://www.fsbooks.top/static/configData/kidrecipe/kid_fuwuxiyi.html";

    public static String PRIVACY_POLICY_URL = "http://www.fsbooks.top/static/configData/kidrecipe/kid_privacy_policy.html";

    public static String ABOUT_URL = "http://www.fsbooks.top/static/configData/kidrecipe/kid_about.html";


    public static String getSavedImgPath(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/ColorBookMain/";
    }

    public static String getSavedCatImgPath(Context context) {
        return context.getFilesDir().getAbsolutePath() + "/ColorBookCat/";
    }

    public static boolean imgSavedOrNot() {
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




}
