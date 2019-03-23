package com.mihwapp.womanrecipe.activitys;

import android.app.Activity;
import android.content.Intent;
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

import com.mihwapp.womanrecipe.R;
import com.mihwapp.womanrecipe.model.bean.RecipeBean;
import com.mihwapp.womanrecipe.view.adapters.CookDetailAdapter;
import com.mihwapp.womanrecipe.view.components.StatusBarUtil;
import com.bumptech.glide.Glide;
//import com.squareup.picasso.Picasso;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_detail);
        ButterKnife.bind(this);
        this.init(savedInstanceState);
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

        Glide.with(this).load(recipeUrl).into(this.imgvBg);

        toolbarLayout.setTitle(data.getName());

        cookDetailAdapter = new CookDetailAdapter(this, data, isShowCollection);

        recyclerList.setLayoutManager(new LinearLayoutManager(this));
        recyclerList.setAdapter(cookDetailAdapter);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
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
}
