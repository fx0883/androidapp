package com.Recipes.app2.activitys;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Recipes.app2.Constants;
import com.Recipes.app2.MainActivity;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;
import com.Recipes.app2.R;


public class SplashActivity extends Activity {

  private ViewGroup container;
  private TextView skipView;
  private ImageView splashHolder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    container = (ViewGroup) this.findViewById(R.id.splash_container);
    skipView = (TextView) findViewById(R.id.skip_view);
    splashHolder = (ImageView) findViewById(R.id.splash_holder);

    new Handler().postDelayed(new Runnable() {
      public void run() {
        goMainScreen();
      }
    }, 3000);
  }

  private void goMainScreen() {
    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
    SplashActivity.this.finish();
  }


  /** 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费 */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

}
