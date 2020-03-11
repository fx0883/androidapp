package com.Recipes.app2.activitys;

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
import android.support.v7.app.AppCompatActivity;
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

import com.Recipes.app2.ConstantAd;
import com.Recipes.app2.ConstantsAdmob;
import com.Recipes.app2.R;
import com.Recipes.app2.model.bean.RecipeBean;
import com.Recipes.app2.utils.SharedPreferencesUtil;
import com.Recipes.app2.utils.WechatUtil;
import com.Recipes.app2.view.adapters.CookDetailAdapter;
import com.Recipes.app2.view.components.StatusBarUtil;
import com.bumptech.glide.Glide;

import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.comm.util.AdError;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
//import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//public class CookDetailActivity extends BaseSwipeBackActivity {
public class CookDetailActivity extends AppCompatActivity {
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


    private RecipeBean data;
    private boolean isShowCollection;

    private CookDetailAdapter cookDetailAdapter;

    @BindView(R.id.bannerContainer)
    ViewGroup bannerContainer;

    BannerView bv = null;



    final int tryloadadMaxTimes = 5;

    int tryloadadTime = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_detail);
        ButterKnife.bind(this);
//        this.setupView();
        this.init(savedInstanceState);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            finish();
//        }
//        else if(id == R.id.recipedetail_action_share){
//            shareToWechat();
////            invokeMini();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void shareToWechat(){

        String appId = "wx32592946de54e8e9"; // 填应用AppId
        IWXAPI api = WXAPIFactory.createWXAPI(this, appId);

        WXMiniProgramObject miniProgramObj = new WXMiniProgramObject();
        miniProgramObj.webpageUrl = "http://www.qq.com"; // 兼容低版本的网页链接
        miniProgramObj.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;// 正式版:0，测试版:1，体验版:2
        miniProgramObj.userName = "gh_7ed7a01ba380";     // 小程序原始id
        miniProgramObj.path = "/pages/recipe/recipe?id="+this.data.getId().toString();
        WXMediaMessage msg = new WXMediaMessage(miniProgramObj);
        msg.title = this.data.getName();                    // 小程序消息title
        msg.description = this.data.getPrompt();               // 小程序消息desc
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

    private byte[] getThumb(){
//        this.mRecipeBean = recipeBean;
        String recipeUrl = "recipesImage/" + this.data.getPhoto();

        Bitmap bmp = this.getImageFromAssetsFile(recipeUrl);

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
        bmp.recycle();

//        WechatUtil
        return WechatUtil.bmpToByteArray(thumbBmp,true);
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

//    @Override
    protected void init(Bundle savedInstanceState){

        StatusBarUtil.setImmersiveStatusBar(this);
        StatusBarUtil.setImmersiveStatusBarToolbar(toolbar, this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        data = intent.getParcelableExtra(Intnet_Data_Cook);
        isShowCollection = intent.getBooleanExtra(Intnet_Data_Collection, false);

        if(null == data)
            return ;

        String recipeUrl = "file:///android_asset/recipesImage/" + data.getPhoto();


//        Picasso.with(this)
//                .load(recipeUrl)
//                .into(this.imgvBg);
        Glide.with(this).load(recipeUrl).into(this.imgvBg);


        getSupportActionBar().setTitle(data.getName());


        cookDetailAdapter = new CookDetailAdapter(this, data, isShowCollection);
//        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerList.setLayoutManager(new LinearLayoutManager(this));
        recyclerList.setAdapter(cookDetailAdapter);


        loadads();
//        imbtnShare.setTranslationY(-50);
    }


    private void loadads(){

        Boolean bIsFirst = SharedPreferencesUtil.getInstance(this).getSPBool("bisfirst");
        if(!bIsFirst){
            return;
        }



        if(tryloadadTime>=tryloadadMaxTimes){
            return;
        }
        tryloadadTime++;
//        int min=0;
//        int max=99;
//        Random random = new Random();
//        int num = random.nextInt(max)%(max-min+1) + min;
//
//        if(num<=80){
//            this.getBanner().loadAD();
//        }
//        else{
//            getAdView();
//        }
//        this.getBanner().loadAD();
//        getAdView();
        this.getBanner().loadAD();
    }

    @OnClick(R.id.btnShare)
    public void onClickShare() {
        //分享当前菜谱
        Log.d("134","134");
    }

    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipedetail_main, menu);
        return true;
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

    private final static String Intnet_Data_Cook = "cook";
    private final static String Intnet_Data_Collection = "collection";
    public static void startActivity(Activity activity, View view, RecipeBean data, boolean isShowCollection){
        Intent intent = new Intent(activity, CookDetailActivity.class);
        intent.putExtra(Intnet_Data_Cook, data);
        intent.putExtra(Intnet_Data_Collection, isShowCollection);

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(activity
                        , Pair.create(view, activity.getString(R.string.transition_cook_detail_imgv_bg))
                        , Pair.create(view, activity.getString(R.string.transition_cook_detail_content))
                        );

        activity.startActivityForResult(intent, 10029, options.toBundle());
    }
    private String getPosID() {

        return ConstantAd.BannerPosID;
    }




    private BannerView getBanner() {

        String posId = getPosID();

        this.bv = new BannerView(this, ADSize.BANNER, ConstantAd.APPID,posId);
        // 注意：如果开发者的banner不是始终展示在屏幕中的话，请关闭自动刷新，否则将导致曝光率过低。
        // 并且应该自行处理：当banner广告区域出现在屏幕后，再手动loadAD。
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

        super.onResume();
    }

    @Override
    protected void onPause() {


        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if(bv!=null) {
            bv.destroy();
        }
        super.onDestroy();
    }
}
