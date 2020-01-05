package com.colorgarden.app6.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class OfflineData {
    public static String MY_PREF = "Offline_Pref";
    public static String KEY_CAT = "category";
    public static String KEY_OFFLINE = "is_offlines";


    public static void setData(String key, String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static boolean isOffline(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_OFFLINE, false);
    }

    public static void setOffLine(Context context, boolean b1) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_OFFLINE, b1);
        editor.apply();
    }

}
