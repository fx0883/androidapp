package com.colorgarden.app6.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.colorgarden.app6.R;
import com.colorgarden.app6.activity.PaintActivity;


public class InternetDialog {
    public static int WIFI_REQ_CODE = 11;
    private static android.support.v7.app.AlertDialog alertDialog;
    @SuppressLint("StaticFieldLeak")
    private static ConnectionDetector connectionDetector;
    private static InternetClick objClick;

    public static void ShowInternetDialog(final Activity activity, final InternetClick click) {
        objClick = click;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity, R.style.MyDialogTheme);
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getResources().getString(R.string.network_err));
        builder.setMessage(activity.getResources().getString(R.string.enable_internet));
        builder.setPositiveButton(activity.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (objClick != null) {
                    objClick.onYesClick();
                }


            }
        });
        builder.setNegativeButton(activity.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                connectionDetector = new ConnectionDetector(activity);
                if (connectionDetector.isConnectingToInternet()) {
                    alertDialog.dismiss();
                } else {
                    ShowInternetDialog(activity, click);
                }
            }
        });
        alertDialog = builder.create();
        alertDialog.show();


    }

    public static void closeInternetDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }

    }

    public interface InternetClick {
        void onYesClick();
    }

}
