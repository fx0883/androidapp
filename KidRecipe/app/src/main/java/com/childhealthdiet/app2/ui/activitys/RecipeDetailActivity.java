package com.ChildHealthDiet.app2.ui.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

//import com.Recipes.app2.Constants;
//import com.Recipes.app2.ConstantsAdmob;
//import com.Recipes.app2.R;
//import com.Recipes.app2.model.bean.RecipeBean;
//import com.Recipes.app2.view.adapters.CookDetailAdapter;
//import com.Recipes.app2.view.components.StatusBarUtil;
import com.ChildHealthDiet.app2.Constants;
import com.ChildHealthDiet.app2.ConstantsAdmob;
import com.bumptech.glide.Glide;
import com.ChildHealthDiet.app2.R;
import com.ChildHealthDiet.app2.adapter.RecipeDetailAdapter;
import com.ChildHealthDiet.app2.model.bean.RecipeBean;
import com.ChildHealthDiet.app2.presenter.RecipeDetailPresenter;
import com.ChildHealthDiet.app2.presenter.contract.RecipeDetailContract;
import com.ChildHealthDiet.app2.ui.base.BaseMVPActivity;
import com.ChildHealthDiet.app2.ui.components.StatusBarUtil;
import com.childhealthdiet.app2.utils.WechatUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.comm.util.AdError;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.AdView;
//import com.qq.e.ads.banner.ADSize;
//import com.qq.e.ads.banner.AbstractBannerADListener;
//import com.qq.e.ads.banner.BannerView;
//import com.qq.e.comm.util.AdError;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import butterknife.BindView;
import com.ChildHealthDiet.app2.utils.SharedPreferencesUtil;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

//import com.squareup.picasso.Picasso;

//public class CookDetailActivity extends BaseSwipeBackActivity {
public class RecipeDetailActivity extends BaseMVPActivity<RecipeDetailContract.Presenter>
        implements RecipeDetailContract.View {
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    public CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.recyclerview_list)
    public RecyclerView recyclerList;

    @BindView(R.id.imgv_bg)
    public ImageView imgvBg;

    @BindView(R.id.btnShare)
    public ImageButton imbtnShare;


    private long recipeId = 0;
    private boolean isShowCollection;

    private RecipeDetailAdapter cookDetailAdapter;

    @BindView(R.id.bannerContainer)
    ViewGroup bannerContainer;

    BannerView bv = null;

    AdView adView = null;

    final int tryloadadMaxTimes = 5;

    int tryloadadTime = 0;

    private RecipeBean mRecipeBean;



    @Override
    protected int getContentId() {
        return R.layout.activity_recipe_detail;
    }


    @Override
    protected void initData(Bundle savedInstanceState){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipedetail_main, menu);

//        MenuItem item = menu.findItem(R.id.recipelist_action_search);
//        searchView.setMenuItem(item);



        return true;
    }
    @Override
    protected void initToolbar(){
        StatusBarUtil.setImmersiveStatusBar(this);
        StatusBarUtil.setImmersiveStatusBarToolbar(toolbar, this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        toolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.black_text));
    }
    /**
     * 初始化零件
     */
    @Override
    protected void initWidget() {

    }
    /**
     * 初始化点击事件
     */
    @Override
    protected void initClick(){

    }
    /**
     * 逻辑使用区
     */
    @Override
    protected void processLogic(){
        super.processLogic();

        Intent intent = getIntent();
        recipeId = intent.getLongExtra(Intnet_Recipe_id,0);
        mPresenter.getRecipeById(this,recipeId);


//        loadads();
    }


    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        else if(id == R.id.recipedetail_action_share){
            shareToWechat();
//            invokeMini();
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareToWechat(){

//        String appId = "wx9666b12631d67687"; // 填应用AppId，APP在开放平台注册的id
//        IWXAPI api = WXAPIFactory.createWXAPI(this, appId);
//
//        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
//        miniProgramObj.webpageUrl = "https://www.fsbooks.top"; // 兼容低版本的网页链接
//        miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
//        miniProgramObj.userName = "gh_5e52bc5b286b";     // 小程序原始id
//        miniProgramObj.path = "/pages/recipe/recipe?id="+this.mRecipeBean.getId().toString();            //小程序页面路径
//        WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
//        msg.title = this.mRecipeBean.getName();                    // 小程序消息title
//        msg.description = this.mRecipeBean.getPrompt();               // 小程序消息desc
//        msg.thumbData = getThumb();                      // 小程序消息封面图片，小于128k
////        msg.thumbData = null;
//
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = "";
//        req.message = msg;
//        req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前支持会话
//        api.sendReq(req);

        String appId = "wx9666b12631d67687"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(this, appId);

        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        miniProgramObj.webpageUrl = "http://www.qq.com"; // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = "gh_5e52bc5b286b";     // 小程序原始id
        miniProgramObj.path = "/pages/recipe/recipe?id="+this.mRecipeBean.getId().toString();
        WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
        msg.title = this.mRecipeBean.getName();                    // 小程序消息title
        msg.description = this.mRecipeBean.getPrompt();               // 小程序消息desc
        msg.thumbData = getThumb();                      // 小程序消息封面图片，小于128k

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("miniProgram");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;  // 目前只支持会话
        api.sendReq(req);


    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void invokeMini(){
        String appId = "wx9666b12631d67687"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(this, appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_5e52bc5b286b"; // 填小程序原始id
        req.path = "/pages/recipe/recipe?id="+this.mRecipeBean.getId().toString();    ;                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }

    public Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    private byte[] getThumb(){
//        this.mRecipeBean = recipeBean;
        String recipeUrl = "recipeimage/" + this.mRecipeBean.getPicture();

        Bitmap bmp = this.getImageFromAssetsFile(recipeUrl);

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
        bmp.recycle();

//        WechatUtil
        return WechatUtil.bmpToByteArray(thumbBmp,true);
    }

    private final static String Intnet_Recipe_id = "recipeId";
    private final static String Intnet_Data_Cook = "cook";
    private final static String Intnet_Data_Collection = "collection";

    public static void startActivity(Activity activity, View view, long recipeId){
        Intent intent = new Intent(activity, RecipeDetailActivity.class);
//        intent.putExtra(Intnet_Data_Cook, data);
        intent.putExtra(Intnet_Recipe_id, recipeId);

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity
                        , Pair.create(view, activity.getString(R.string.transition_cook_detail_imgv_bg))
                        , Pair.create(view, activity.getString(R.string.transition_cook_detail_content))
                );

        activity.startActivityForResult(intent, 10029, options.toBundle());
    }

    @Override
    public void updateRecipe(RecipeBean recipeBean) {
        this.mRecipeBean = recipeBean;
        String recipeUrl = "file:///android_asset/recipeimage/" + recipeBean.getPicture();
        Glide.with(this).load(recipeUrl).into(this.imgvBg);
        getSupportActionBar().setTitle(recipeBean.getName());
        toolbarLayout.setTitle(recipeBean.getName());
        cookDetailAdapter = new RecipeDetailAdapter(this, recipeBean,mPresenter,isShowCollection);
        recyclerList.setLayoutManager(new LinearLayoutManager(this));
        recyclerList.setAdapter(cookDetailAdapter);
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }


    @Override
    protected RecipeDetailContract.Presenter bindPresenter() {
        return new RecipeDetailPresenter();
    }


    private void loadads(){

        Boolean bIsFirst = SharedPreferencesUtil.getInstance(this).getSPBool("bisfirst");
        if(!bIsFirst){
            return;
        }
        if(this.adView != null || this.bv != null)
        {
            bannerContainer.removeAllViews();
        }

        if(tryloadadTime>=tryloadadMaxTimes){
            return;
        }
        tryloadadTime++;
        int min=0;
        int max=99;
        Random random = new Random();
        int num = random.nextInt(max)%(max-min+1) + min;

        if(num<=90){
            this.getBanner().loadAD();
        }
        else{
            getAdView();
        }

    }

    private String getPosID() {

        return Constants.BannerPosID;
    }

    AdView getAdView(){
        adView = new AdView(RecipeDetailActivity.this);

        adView.setAdUnitId(ConstantsAdmob.BannerPosID);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                tryloadadTime = 0;
                super.onAdLoaded();

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous banner ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous banner ad failed to load. Attempting to"
                        + " load the next banner ad in the items list.");
                loadads();
            }
        });

        // Load the banner ad.
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.loadAd(new AdRequest.Builder().build());
        bannerContainer.addView(adView);

        return adView;

    }


    private BannerView getBanner() {

        String posId = getPosID();

        this.bv = new BannerView(this, ADSize.BANNER, Constants.APPID,posId);
        bv.setRefresh(30);
        bv.setADListener(new AbstractBannerADListener() {

            @Override
            public void onNoAD(AdError error) {
                tryloadadTime = 0;
                Log.i(
                        "AD_DEMO",
                        String.format("Banner onNoAD，eCode = %d, eMsg = %s", error.getErrorCode(),
                                error.getErrorMsg()));
                loadads();
            }

            @Override
            public void onADReceiv() {

                Log.i("AD_DEMO", "ONBannerReceive");
            }
        });
        bannerContainer.addView(bv);
        return this.bv;
    }

    @Override
    protected void onResume() {
        if(adView!=null){
            adView.resume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {

        if(adView!=null){
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(adView!=null){
            adView.destroy();
        }
        if(bv!=null) {
            bv.destroy();
        }
        super.onDestroy();
    }

}
