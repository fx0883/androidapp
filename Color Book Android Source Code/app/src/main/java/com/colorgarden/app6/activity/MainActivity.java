package com.colorgarden.app6.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.colorgarden.app6.R;


import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.img_start)
    ImageView img_start;
    @BindView(R.id.img_mywork)
    ImageView img_myWork;
    @BindView(R.id.img_feedback)
    ImageView img_feedback;
    @BindView(R.id.img_rate)
    ImageView img_rate;
    PermissionActivity permissionActivity;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);

        ButterKnife.bind(this);
        permissionActivity = new PermissionActivity(this);

//        NoInternetDialog noInternetDialog = new NoInternetDialog.Builder(MainActivity.this).build();
//        noInternetDialog.show();

        if (!PermissionActivity.checkPermission()) {
            permissionActivity.requestPermission();
        }

        img_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityImageCategory.class);
                startActivity(intent);
            }
        });

        img_myWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreationActivity.class);
                startActivity(intent);
            }
        });

        img_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getPackageName();
                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));

                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } catch (android.content.ActivityNotFoundException Info) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));

//                    Toast.s("您的手机没有安装Android应用市场");

                    android.widget.Toast.makeText(MainActivity.this,"您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();


                    Info.printStackTrace();

                }



            }
        });

        img_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback = "";
                sendFeedback(feedback);
            }
        });


    }


    // Send FeedBack
    private void sendFeedback(String feedback) {
        Intent localIntent = new Intent(Intent.ACTION_SEND);
        localIntent.putExtra(Intent.EXTRA_EMAIL, R.string.mail_feedback_email);
        localIntent.putExtra(Intent.EXTRA_CC, "");
        String str;
        try {
            str = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            localIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for ColorBook");
            localIntent.putExtra(Intent.EXTRA_TEXT, "\n\n----------------------------------\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + str + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER + "Feedback: " + feedback);
            localIntent.setType("message/rfc822");
            startActivity(Intent.createChooser(localIntent, "Choose an Email client :"));
        } catch (Exception e) {
            Log.d("OpenFeedback", e.getMessage());
        }
    }


    // Exit Dialog
    public void showExitDialog() {



        new android.support.v7.app.AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(R.string.msg_main_exitApp)
                .setPositiveButton(R.string.btn_main_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finishAffinity();
                    }

                })
                .setNegativeButton(R.string.btn_main_no, null)
                .show();
//        new AlertDialog.Builder(this)
//                .setTitle(getResources().getString(R.string.app_name))
//                .setMessage(R.string.msg_main_exitApp)
//                .setPositiveButton(R.string.btn_main_yes, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        MainActivity.this.finishAffinity();
//                    }
//
//                })
//                .setNegativeButton(R.string.btn_main_no, null)
//                .show();
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onBackPressed() {
        showExitDialog();
    }
}
