package com.childhealthdiet.app2.ui.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

//import com.Recipes.app2.Constants;
//import com.Recipes.app2.ConstantsAdmob;
//import com.Recipes.app2.R;
//import com.Recipes.app2.model.bean.RecipeBean;
//import com.Recipes.app2.view.adapters.CookDetailAdapter;
//import com.Recipes.app2.view.components.StatusBarUtil;
import com.bumptech.glide.Glide;
import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.adapter.RecipeDetailAdapter;
import com.childhealthdiet.app2.model.bean.RecipeBean;
import com.childhealthdiet.app2.presenter.RecipeDetailPresenter;
import com.childhealthdiet.app2.presenter.contract.RecipeDetailContract;
import com.childhealthdiet.app2.presenter.contract.RecipeKeywordListContract;
import com.childhealthdiet.app2.ui.base.BaseMVPActivity;
import com.childhealthdiet.app2.ui.components.StatusBarUtil;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdSize;
//import com.google.android.gms.ads.AdView;
//import com.qq.e.ads.banner.ADSize;
//import com.qq.e.ads.banner.AbstractBannerADListener;
//import com.qq.e.ads.banner.BannerView;
//import com.qq.e.comm.util.AdError;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    protected void initData(Bundle savedInstanceState){

//        StatusBarUtil.setImmersiveStatusBar(this);
//        StatusBarUtil.setImmersiveStatusBarToolbar(toolbar, this);
//
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null)
//            actionBar.setDisplayHomeAsUpEnabled(true);




//        loadads();
//        imbtnShare.setTranslationY(-50);
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
        long recipeId = intent.getLongExtra(Intnet_Recipe_id,0);
        mPresenter.getRecipeById(this,recipeId);



    }







//    private void loadads(){
//
//        if(this.adView != null || this.bv != null)
//        {
//            bannerContainer.removeAllViews();
//        }
//
//        if(tryloadadTime>=tryloadadMaxTimes){
//            return;
//        }
//        tryloadadTime++;
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
//
////        getAdView();
//    }

//    @OnClick(R.id.btnShare)
//    public void onClickShare() {
//        //分享当前菜谱
//        Log.d("134","134");
//    }

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

        return super.onOptionsItemSelected(item);
    }

    private final static String Intnet_Recipe_id = "recipeId";
    private final static String Intnet_Data_Cook = "cook";
    private final static String Intnet_Data_Collection = "collection";
//    public static void startActivity(Activity activity, View view, RecipeBean data, boolean isShowCollection){
//        Intent intent = new Intent(activity, RecipeDetailActivity.class);
////        intent.putExtra(Intnet_Data_Cook, data);
//        intent.putExtra(Intnet_Data_Collection, isShowCollection);
//
//        ActivityOptionsCompat options =
//                ActivityOptionsCompat.makeSceneTransitionAnimation(activity
//                        , Pair.create(view, activity.getString(R.string.transition_cook_detail_imgv_bg))
//                        , Pair.create(view, activity.getString(R.string.transition_cook_detail_content))
//                        );
//
//        activity.startActivityForResult(intent, 10029, options.toBundle());
//    }

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
        String recipeUrl = "file:///android_asset/recipeimage/" + recipeBean.getPicture();
        Glide.with(this).load(recipeUrl).into(this.imgvBg);
        getSupportActionBar().setTitle(recipeBean.getName());
        toolbarLayout.setTitle(recipeBean.getName());
//        cookDetailAdapter = new RecipeDetailAdapter(this, recipeBean,isShowCollection);
        cookDetailAdapter = new RecipeDetailAdapter(this, recipeBean,mPresenter,isShowCollection);
//        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerList.setLayoutManager(new LinearLayoutManager(this));
        recyclerList.setAdapter(cookDetailAdapter);
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onStop(){
//        super.onStop();
////        mPresenter.updateRecipeBeanData(this,this.cookDetailAdapter.srcData);
//    }

    @Override
    protected RecipeDetailContract.Presenter bindPresenter() {
        return new RecipeDetailPresenter();
    }
//    private String getPosID() {
//
//        return Constants.BannerPosID;
//    }

//    AdView getAdView(){
//        adView = new AdView(CookDetailActivity.this);
//
//        adView.setAdUnitId(ConstantsAdmob.BannerPosID);
////        recyclerViewItems.add(i, adView);
//
//
//
//
//        // Set an AdListener on the AdView to wait for the previous banner ad
//        // to finish loading before loading the next ad in the items list.
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                tryloadadTime = 0;
//                super.onAdLoaded();
//                // The previous banner ad loaded successfully, call this method again to
//                // load the next ad in the items list.
////                loadBannerAd(index + ITEMS_PER_AD);
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // The previous banner ad failed to load. Call this method again to load
//                // the next ad in the items list.
//                Log.e("MainActivity", "The previous banner ad failed to load. Attempting to"
//                        + " load the next banner ad in the items list.");
////                loadBannerAd(index + ITEMS_PER_AD);
//                loadads();
////                ERROR_CODE_INTERNAL_ERROR
//            }
//        });
//
//        // Load the banner ad.
//        adView.setAdSize(AdSize.BANNER);
//        adView.loadAd(new AdRequest.Builder().build());
//        bannerContainer.addView(adView);
//
//        return adView;
//
//    }
//
//
//    private BannerView getBanner() {
//
//        String posId = getPosID();
//
//        this.bv = new BannerView(this, ADSize.BANNER, Constants.APPID,posId);
//        // 注意：如果开发者的banner不是始终展示在屏幕中的话，请关闭自动刷新，否则将导致曝光率过低。
//        // 并且应该自行处理：当banner广告区域出现在屏幕后，再手动loadAD。
//        bv.setRefresh(30);
//        bv.setADListener(new AbstractBannerADListener() {
//
//            @Override
//            public void onNoAD(AdError error) {
//                tryloadadTime = 0;
//                Log.i(
//                        "AD_DEMO",
//                        String.format("Banner onNoAD，eCode = %d, eMsg = %s", error.getErrorCode(),
//                                error.getErrorMsg()));
//                loadads();
//            }
//
//            @Override
//            public void onADReceiv() {
//
//                Log.i("AD_DEMO", "ONBannerReceive");
//            }
//        });
//        bannerContainer.addView(bv);
//        return this.bv;
//    }

//    @Override
//    protected void onResume() {
//        if(adView!=null){
//            adView.resume();
//        }
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//
//        if(adView!=null){
//            adView.pause();
//        }
//        super.onPause();
//    }
//
//    @Override
//    protected void onDestroy() {
//        if(adView!=null){
//            adView.destroy();
//        }
//        if(bv!=null) {
//            bv.destroy();
//        }
//        super.onDestroy();
//    }
}
