package com.ChildHealthDiet.app2.utils;

import android.app.Dialog;
import android.content.Context;

import com.ChildHealthDiet.app2.R;


public class PrivacyDialog extends Dialog {

    public PrivacyDialog(Context context) {
        super(context, R.style.PrivacyThemeDialog);

        setContentView(R.layout.dialog_privacy);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }
}
