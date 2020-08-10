package com.ChildHealthDiet.app2.ui.activitys;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.ChildHealthDiet.app2.R;
import com.kongzue.dialog.v3.WaitDialog;

import java.util.Objects;

public class WebViewActivity extends AppCompatActivity {


    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initToolbar();
        loadWebview();
    }

    void initToolbar(){
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void loadWebview(){


        TextView textViewTitle = (TextView) findViewById(R.id.webview_toolbar_title_id);

        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        textViewTitle.setText(title);
        WebView webView = (WebView) findViewById(R.id.wv_webview);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
//                dialog.show();
                WaitDialog.show(WebViewActivity.this, "Loading");
            }
            @Override
            public void onPageFinished(WebView view, String url) {
//                dialog.dismiss();
                WaitDialog.dismiss();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);//当打开新的连接时,使用当前的webview,不使用系统其他浏览器
                return true;
            }
        });



        webView.loadUrl(url);
//系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                //使用WebView加载显示url
//                view.loadUrl(url);
//                //返回true
//                return true;
//            }
//        });
    }
//
//    private void initWebViewChromeClient(final WebView webView) {
//        webView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//
//
//                WaitDialog.show(me, "测试").setOnBackClickListener(new OnBackClickListener() {
//                    @Override
//                    public boolean onBackClick() {
//                        toast("按下返回！");
//                        return false;
//                    }
//                });
//                WaitDialog.dismiss(3000);
//                if (newProgress == 100) {
//                    progressBar.setVisibility(View.GONE);
//                    progressBar.setProgress(0);
//                    if (isPlay) {
//                        webView.loadUrl("javascript:audio.cutoff(" + true + ")");//暂停播放
//                    }
//                }
//                super.onProgressChanged(view, newProgress);
//            }
//
//        });
//    }
}
