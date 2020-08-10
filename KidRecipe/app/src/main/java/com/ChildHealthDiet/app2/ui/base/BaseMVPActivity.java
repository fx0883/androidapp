package com.ChildHealthDiet.app2.ui.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ChildHealthDiet.app2.R;
import com.ChildHealthDiet.app2.constant.Constant;
import com.ChildHealthDiet.app2.constant.ConstantAd;
import com.ChildHealthDiet.app2.utils.AdUtils;
import com.ChildHealthDiet.app2.utils.PrivacyDialog;
import com.ChildHealthDiet.app2.utils.SPUtil;
import com.miui.zeus.mimo.sdk.InterstitialAd;
import com.miui.zeus.mimo.sdk.view.WebViewActivity;

/**
 * Created by newbiechen on 17-4-25.
 */

public abstract class BaseMVPActivity<T extends BaseContract.BasePresenter> extends BaseActivity{

    protected T mPresenter;

    protected abstract T bindPresenter();

    @Override
    protected void processLogic() {
        attachView(bindPresenter());
    }

    private void attachView(T presenter){
        mPresenter = presenter;
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        try {
            mInterstitialAd.destroy();
        } catch (Exception e) {
        }
    }


    //加入小米广告
    private InterstitialAd mInterstitialAd = null;

    protected void fetchAd() {
        if(!AdUtils.getInstance(this).isbIsShowInterteristal()){
            return;
        }
        if(mInterstitialAd == null){
            mInterstitialAd = new InterstitialAd(this);
        }

        mInterstitialAd.loadAd(ConstantAd.InterteristalPosID, new InterstitialAd.InterstitialAdLoadListener() {
            @Override
            public void onAdLoadSuccess() {
//                        mShowBtn.setEnabled(true);
                BaseMVPActivity.this.showAd();
            }

            @Override
            public void onAdLoadFailed(int errorCode, String errorMsg) {

            }
        });
    }

    protected void showAd() {

        mInterstitialAd.show(mInterstitialAdInteractionListener);
    }

    private InterstitialAd.InterstitialAdInteractionListener mInterstitialAdInteractionListener
            = new InterstitialAd.InterstitialAdInteractionListener() {
        @Override
        public void onAdClick() {

        }

        @Override
        public void onAdShow() {


        }

        @Override
        public void onAdClosed() {



        }

        @Override
        public void onRenderFail(int code, String msg) {


        }
    };





}