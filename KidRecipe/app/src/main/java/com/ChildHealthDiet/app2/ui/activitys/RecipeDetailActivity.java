package com.ChildHealthDiet.app2.ui.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.ChildHealthDiet.app2.utils.WechatUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

//import com.Recipes.app2.Constants;
//import com.Recipes.app2.ConstantsAdmob;
//import com.Recipes.app2.R;
//import com.Recipes.app2.model.bean.RecipeBean;
//import com.Recipes.app2.view.adapters.CookDetailAdapter;
//import com.Recipes.app2.view.components.StatusBarUtil;
import com.bumptech.glide.Glide;
import com.ChildHealthDiet.app2.R;
import com.ChildHealthDiet.app2.adapter.RecipeDetailAdapter;
import com.ChildHealthDiet.app2.model.bean.RecipeBean;
import com.ChildHealthDiet.app2.presenter.RecipeDetailPresenter;
import com.ChildHealthDiet.app2.presenter.contract.RecipeDetailContract;
import com.ChildHealthDiet.app2.ui.base.BaseMVPActivity;
import com.ChildHealthDiet.app2.ui.components.StatusBarUtil;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.AdView;
//import com.qq.e.ads.banner.ADSize;
//import com.qq.e.ads.banner.AbstractBannerADListener;
//import com.qq.e.ads.banner.BannerView;
//import com.qq.e.comm.util.AdError;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;

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


    private long recipeId;
    private boolean isShowCollection;

    private RecipeDetailAdapter cookDetailAdapter;

//    @BindView(R.id.bannerContainer)
//    ViewGroup bannerContainer;
//
//    BannerView bv = null;
//
//    AdView adView = null;

    final int tryloadadMaxTimes = 5;

    int tryloadadTime = 0;

    private RecipeBean mRecipeBean;


    @Override
    protected int getContentId() {
        return R.layout.activity_recipe_detail;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_cook_detail);
//        ButterKnife.bind(this);
////        this.setupView();
//        this.init(savedInstanceState);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipedetail_main, menu);
        return true;
    }

    @Override
    protected void initData(Bundle savedInstanceState){

        super.initData(savedInstanceState);
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
        super.initWidget();
    }
    /**
     * 初始化点击事件
     */
    @Override
    protected void initClick(){
        super.initClick();
    }
    /**
     * 逻辑使用区
     */
    @Override
    protected void processLogic(){
        super.processLogic();

        Intent intent = getIntent();
        long recipeId = intent.getLongExtra(Intnet_Recipe_id,0);
        mPresenter.getRecipeById(this,recipeId);

        fetchAd();

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

    private final static String Intnet_Recipe_id = "recipeId";
    private final static String Intnet_Data_Cook = "cook";
    private final static String Intnet_Data_Collection = "collection";

    public static void startActivity(Activity activity, View view, long recipeId){
        Intent intent = new Intent(activity, RecipeDetailActivity.class);
        intent.putExtra(Intnet_Recipe_id, recipeId);

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity
                        , Pair.create(view, activity.getString(R.string.transition_cook_detail_imgv_bg))
                        , Pair.create(view, activity.getString(R.string.transition_cook_detail_content))
                );

        activity.startActivityForResult(intent, 10029, options.toBundle());
    }

//    @Override
//    public void updateRecipe(RecipeBean recipeBean) {
//        String recipeUrl = "file:///android_asset/recipeimage/" + recipeBean.getPicture();
//        Glide.with(this).load(recipeUrl).into(this.imgvBg);
//        getSupportActionBar().setTitle(recipeBean.getName());
//        toolbarLayout.setTitle(recipeBean.getName());
////        cookDetailAdapter = new RecipeDetailAdapter(this, recipeBean,isShowCollection);
//        cookDetailAdapter = new RecipeDetailAdapter(this, recipeBean,mPresenter,isShowCollection);
////        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerList.setLayoutManager(new LinearLayoutManager(this));
//        recyclerList.setAdapter(cookDetailAdapter);
//    }

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


    private void shareToWechat(){

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



}