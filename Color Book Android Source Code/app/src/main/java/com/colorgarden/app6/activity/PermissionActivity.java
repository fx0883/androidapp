package com.colorgarden.app6.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

@SuppressLint("Registered")
public class PermissionActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public final int PERMISSION_REQ = 111;

    PermissionActivity(Context context) {
        PermissionActivity.context = context;
    }

    public static boolean checkPermission() {
        int read;
        read = ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE);
        int write = ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE);
        return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED;
    }


    public void requestPermission() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
        }, PERMISSION_REQ);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQ) {
            if (grantResults.length > 0) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    Log.e("", "");
                }
            }
        }
    }

}
