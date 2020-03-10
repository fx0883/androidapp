package com.childhealthdiet.app2;

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
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.colorgarden.app6.MainActivity;
//import com.colorgarden.app6.R;
//import com.colorgarden.app6.constant.ConstantAd;
//import com.colorgarden.app6.utils.AdUtils;
import com.ChildHealthDiet.app2.ConstantAd;
import com.ChildHealthDiet.app2.MainActivity;
import com.ChildHealthDiet.app2.R;
import com.childhealthdiet.app2.utils.AdUtils;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;

/**
 * 这是demo工程的入口Activity，在这里会首次调用广点通的SDK。
 *
 * 在调用SDK之前，如果您的App的targetSDKVersion >= 23，那么建议动态申请相关权限。
 */
public class SplashActivity extends Activity implements SplashADListener, View.OnClickListener {

  private SplashAD splashAD;
  private ViewGroup container;
  private TextView skipView;
  private ImageView splashHolder;
  private static final String SKIP_TEXT = "点击跳过 %d";

  public boolean canJump = false;
  private boolean needStartDemoList = true;

  private boolean loadAdOnly = false;
  private boolean showingAd = false;
  private LinearLayout loadAdOnlyView;
  private Button loadAdOnlyCloseButton;
  private Button loadAdOnlyDisplayButton;
  private Button loadAdOnlyRefreshButton;
  private TextView loadAdOnlyStatusTextView;

  /**
   * 为防止无广告时造成视觉上类似于"闪退"的情况，设定无广告时页面跳转根据需要延迟一定时间，demo
   * 给出的延时逻辑是从拉取广告开始算开屏最少持续多久，仅供参考，开发者可自定义延时逻辑，如果开发者采用demo
   * 中给出的延时逻辑，也建议开发者考虑自定义minSplashTimeWhenNoAD的值（单位ms）
   **/
  private int minSplashTimeWhenNoAD = 2000;
  /**
   * 记录拉取广告的时间
   */
  private long fetchSplashADTime = 0;
  private Handler handler = new Handler(Looper.getMainLooper());

  private boolean bIsShowAd = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    bIsShowAd = AdUtils.getInstance(this).isbIsShowAd();

    container = (ViewGroup) this.findViewById(R.id.splash_container);
    boolean customSkipBtn = getIntent().getBooleanExtra("custom_skip_btn", false);
    if (customSkipBtn) {
      skipView = (TextView) findViewById(R.id.skip_view);
      skipView.setVisibility(View.VISIBLE);
    }
    splashHolder = (ImageView) findViewById(R.id.splash_holder);
    boolean needLogo = getIntent().getBooleanExtra("need_logo", true);
//    needStartDemoList = getIntent().getBooleanExtra("need_start_demo_list", true);
    loadAdOnly = getIntent().getBooleanExtra("load_ad_only", false);

    loadAdOnlyView = findViewById(R.id.splash_load_ad_only);
    loadAdOnlyCloseButton = findViewById(R.id.splash_load_ad_close);
    loadAdOnlyCloseButton.setOnClickListener(this);
    loadAdOnlyDisplayButton = findViewById(R.id.splash_load_ad_display);
    loadAdOnlyDisplayButton.setOnClickListener(this);
    loadAdOnlyRefreshButton = findViewById(R.id.splash_load_ad_refresh);
    loadAdOnlyRefreshButton.setOnClickListener(this);
    loadAdOnlyStatusTextView = findViewById(R.id.splash_load_ad_status);

    if(loadAdOnly){
      loadAdOnlyView.setVisibility(View.VISIBLE);
      loadAdOnlyStatusTextView.setText(R.string.splash_loading);
      loadAdOnlyDisplayButton.setEnabled(false);
    }
    if (!needLogo) {
      findViewById(R.id.app_logo).setVisibility(View.GONE);
    }
    if (Build.VERSION.SDK_INT >= 23) {
      checkAndRequestPermission();
    } else {
      // 如果是Android6.0以下的机器，建议在manifest中配置相关权限，这里可以直接调用SDK
      fetchSplashAD(this, container, skipView, ConstantAd.APPID, getPosId(), this, 0);
    }
  }

  private String getPosId() {

    return ConstantAd.SplashPosID;
  }

  /**
   *
   * ----------非常重要----------
   *
   * Android6.0以上的权限适配简单示例：
   *
   * 如果targetSDKVersion >= 23，那么建议动态申请相关权限，再调用广点通SDK
   *
   * SDK不强制校验下列权限（即:无下面权限sdk也可正常工作），但建议开发者申请下面权限，尤其是READ_PHONE_STATE权限
   *
   * READ_PHONE_STATE权限用于允许SDK获取用户标识,
   * 针对单媒体的用户，允许获取权限的，投放定向广告；不允许获取权限的用户，投放通投广告，媒体可以选择是否把用户标识数据提供给优量汇，并承担相应广告填充和eCPM单价下降损失的结果。
   *
   * Demo代码里是一个基本的权限申请示例，请开发者根据自己的场景合理地编写这部分代码来实现权限申请。
   * 注意：下面的`checkSelfPermission`和`requestPermissions`方法都是在Android6.0的SDK中增加的API，如果您的App还没有适配到Android6.0以上，则不需要调用这些方法，直接调用广点通SDK即可。
   */
  @TargetApi(Build.VERSION_CODES.M)
  private void checkAndRequestPermission() {
    List<String> lackedPermission = new ArrayList<String>();
    if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
      lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
    }

    if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
      lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    // 快手SDK所需相关权限，存储权限，此处配置作用于流量分配功能，关于流量分配，详情请咨询商务;如果您的APP不需要快手SDK的流量分配功能，则无需申请SD卡权限
    if (!(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED )){
      lackedPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
    }
    if (!(checkSelfPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
      lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    // 如果需要的权限都已经有了，那么直接调用SDK
    if (lackedPermission.size() == 0) {
      fetchSplashAD(this, container, skipView, ConstantAd.APPID, getPosId(), this, 0);
    } else {
      // 否则，建议请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限
      String[] requestPermissions = new String[lackedPermission.size()];
      lackedPermission.toArray(requestPermissions);
      requestPermissions(requestPermissions, 1024);
    }
  }

  private boolean hasAllPermissionsGranted(int[] grantResults) {
    for (int grantResult : grantResults) {
      if (grantResult == PackageManager.PERMISSION_DENIED) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
      fetchSplashAD(this, container, skipView, ConstantAd.APPID, ConstantAd.SplashPosID, this, 0);
    } else {
      Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
      Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
      intent.setData(Uri.parse("package:" + getPackageName()));
      startActivity(intent);
      finish();
    }
  }

  /**
   * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
   *
   * @param activity        展示广告的activity
   * @param adContainer     展示广告的大容器
   * @param skipContainer   自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
   * @param appId           应用ID
   * @param posId           广告位ID
   * @param adListener      广告状态监听器
   * @param fetchDelay      拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
   */
  private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                             String appId, String posId, SplashADListener adListener, int fetchDelay) {
    if(!bIsShowAd){
      onNoAD(null);
    }
    else{
      fetchSplashADTime = System.currentTimeMillis();
      splashAD = new SplashAD(activity, skipContainer, appId, posId, adListener, fetchDelay);
      if(loadAdOnly) {
        splashAD.fetchAdOnly();
      }else{
        splashAD.fetchAndShowIn(adContainer);
      }
    }

  }

  @Override
  public void onADPresent() {
    Log.i("AD_DEMO", "SplashADPresent");
  }

  @Override
  public void onADClicked() {
    Log.i("AD_DEMO", "SplashADClicked clickUrl: "
        + (splashAD.getExt() != null ? splashAD.getExt().get("clickUrl") : ""));
  }

  /**
   * 倒计时回调，返回广告还将被展示的剩余时间。
   * 通过这个接口，开发者可以自行决定是否显示倒计时提示，或者还剩几秒的时候显示倒计时
   *
   * @param millisUntilFinished 剩余毫秒数
   */
  @Override
  public void onADTick(long millisUntilFinished) {
    Log.i("AD_DEMO", "SplashADTick " + millisUntilFinished + "ms");
    if (skipView != null) {
      skipView.setText(String.format(SKIP_TEXT, Math.round(millisUntilFinished / 1000f)));
    }
  }

  @Override
  public void onADExposure() {
    Log.i("AD_DEMO", "SplashADExposure");
  }

  @Override
  public void onADLoaded(long expireTimestamp) {
    Log.i("AD_DEMO", "SplashADFetch expireTimestamp:"+expireTimestamp);

    if(loadAdOnly) {
      loadAdOnlyDisplayButton.setEnabled(true);
      long timeIntervalSec = (expireTimestamp- SystemClock.elapsedRealtime())/1000;
      long min = timeIntervalSec/60;
      long second = timeIntervalSec-(min*60);
      loadAdOnlyStatusTextView.setText("加载成功,广告将在:"+min+"分"+second+"秒后过期，请在此之前展示(showAd)");
    }
  }

  @Override
  public void onADDismissed() {
    Log.i("AD_DEMO", "SplashADDismissed");
    next();
  }

  @Override
  public void onNoAD(AdError error) {
//    String str = String.format("LoadSplashADFail, eCode=%d, errorMsg=%s", error.getErrorCode(),
//        error.getErrorMsg());
//    Log.i("AD_DEMO",str);
//    handler.post(new Runnable() {
//      @Override
//      public void run() {
//        Toast.makeText(SplashActivity.this.getApplicationContext(), str, Toast.LENGTH_SHORT).show();
//      }
//    });
//    if(loadAdOnly && !showingAd) {
//      loadAdOnlyStatusTextView.setText(str);
//      return;//只拉取广告时，不终止activity
//    }
    /**
     * 为防止无广告时造成视觉上类似于"闪退"的情况，设定无广告时页面跳转根据需要延迟一定时间，demo
     * 给出的延时逻辑是从拉取广告开始算开屏最少持续多久，仅供参考，开发者可自定义延时逻辑，如果开发者采用demo
     * 中给出的延时逻辑，也建议开发者考虑自定义minSplashTimeWhenNoAD的值
     **/
    long alreadyDelayMills = System.currentTimeMillis() - fetchSplashADTime;//从拉广告开始到onNoAD已经消耗了多少时间
    long shouldDelayMills = alreadyDelayMills > minSplashTimeWhenNoAD ? 0 : minSplashTimeWhenNoAD
        - alreadyDelayMills;//为防止加载广告失败后立刻跳离开屏可能造成的视觉上类似于"闪退"的情况，根据设置的minSplashTimeWhenNoAD
    // 计算出还需要延时多久
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        if (needStartDemoList) {
          SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        SplashActivity.this.finish();
      }
    }, shouldDelayMills);
  }

  /**
   * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
   * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
   */
  private void next() {
    if (canJump) {
      if (needStartDemoList) {
        this.startActivity(new Intent(this, MainActivity.class));
      }
      this.finish();
    } else {
      canJump = true;
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    canJump = false;
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (canJump) {
      next();
    }
    canJump = true;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    handler.removeCallbacksAndMessages(null);
  }

  /** 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费 */
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
      if(keyCode == KeyEvent.KEYCODE_BACK && loadAdOnlyView.getVisibility() == View.VISIBLE){
        return super.onKeyDown(keyCode, event);
      }
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.splash_load_ad_close:
        this.finish();
        break;
      case R.id.splash_load_ad_refresh:
        showingAd = false;
        splashAD.fetchAdOnly();
        this.loadAdOnlyStatusTextView.setText(R.string.splash_loading);
        loadAdOnlyDisplayButton.setEnabled(false);
        break;
      case R.id.splash_load_ad_display:
        loadAdOnlyView.setVisibility(View.GONE);
        showingAd = true;
        splashAD.showAd(container);
        break;
      default:
    }
  }
}
