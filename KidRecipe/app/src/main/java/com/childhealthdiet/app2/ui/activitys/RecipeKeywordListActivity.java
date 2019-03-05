package com.childhealthdiet.app2.ui.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.RecipeApplication;
import com.childhealthdiet.app2.adapter.MonthRecipeDataAdapter;
import com.childhealthdiet.app2.adapter.RecipeListDataAdapter;
import com.childhealthdiet.app2.model.bean.MonthRecipe;
import com.childhealthdiet.app2.model.bean.RecipeBean;
import com.childhealthdiet.app2.presenter.RecipeKeywordListPresenter;
import com.childhealthdiet.app2.presenter.contract.RecipeKeywordListContract;
import com.childhealthdiet.app2.ui.base.BaseMVPActivity;
import com.childhealthdiet.app2.ui.categorys.RECIPETYPE;
import com.childhealthdiet.app2.utils.RecycleViewDivider;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RecipeKeywordListActivity extends BaseMVPActivity<RecipeKeywordListContract.Presenter>
        implements RecipeKeywordListContract.View {

    private static final String DATA_KEY = "data_Key";
    private static final String RECIPE_TYPE_KEY = "recipe_Type_Key";

    private static final String KEYWORD_KEY = "keyword_key";


    private MonthRecipe mMonthRecipe = null;
    private String mKeyword = null;
    private RECIPETYPE mRecipeType = RECIPETYPE.None;
    @BindView(R.id.recipelist_recycler_view)
    LRecyclerView mRecyclerView;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private RecipeListDataAdapter mRecipeListDataAdapter = null;

    @BindView(R.id.recipelist_toolbar_id)
    Toolbar mToolbar;
    @BindView(R.id.recipelist_toolbar_title_id)
    TextView mToolbarTitle;

    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    @BindView(R.id.llBatchManagement)
    LinearLayout llBatchManagement;

    @BindView(R.id.tvSelectAll)
    TextView tvSelectAll;



    private boolean mIsCollectModel = false;


    public boolean ismIsCollectModel() {
        return mIsCollectModel;
    }

    public static void startActivity(Context context, RECIPETYPE recipetype, MonthRecipe monthRecipe){
        Intent intent  =new Intent(context,RecipeKeywordListActivity.class);
        intent.putExtra(RECIPE_TYPE_KEY,recipetype.ordinal());
        intent.putExtra(DATA_KEY,monthRecipe);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, RECIPETYPE recipetype, String strKey){
        Intent intent  =new Intent(context,RecipeKeywordListActivity.class);
        intent.putExtra(RECIPE_TYPE_KEY,recipetype.ordinal());
        intent.putExtra(KEYWORD_KEY,strKey);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, RECIPETYPE recipetype){
        Intent intent  =new Intent(context,RecipeKeywordListActivity.class);
        intent.putExtra(RECIPE_TYPE_KEY,recipetype.ordinal());
        context.startActivity(intent);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_recipe_keyword_list;
    }

    @Override
    public void updateRecipe(List<RecipeBean> recipeBeans) {
        mRecipeListDataAdapter.setDataList(recipeBeans);
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    protected RecipeKeywordListContract.Presenter bindPresenter() {
        return new RecipeKeywordListPresenter();
    }


    /**
     * 配置Toolbar
     * @param toolbar
     */
    @Override
    protected void setUpToolbar(Toolbar toolbar){

    }


    @Override
    protected void initData(Bundle savedInstanceState){
        super.initData(savedInstanceState);
        if (savedInstanceState != null){
            mRecipeType = RECIPETYPE.values()[savedInstanceState.getInt(RECIPE_TYPE_KEY)];
            switch (mRecipeType) {
                case None:
                    break;
                case Month:
                    mMonthRecipe = savedInstanceState.getParcelable(DATA_KEY);
                case Category:

                    break;
                case Keyword:
                    mKeyword = savedInstanceState.getString(KEYWORD_KEY);
                    break;
                case Collect:
                    collectMode(true);
                    break;
                case Symptoms:
                    mKeyword = getIntent().getStringExtra(KEYWORD_KEY);
                    break;
                case Eattime:
                    mKeyword = getIntent().getStringExtra(KEYWORD_KEY);
                    break;
                case Type:
                    mKeyword = getIntent().getStringExtra(KEYWORD_KEY);
                    break;
                default:
                    break;
            }
        }
        else {

            mRecipeType = RECIPETYPE.values()[getIntent().getIntExtra(RECIPE_TYPE_KEY, 0)];
            switch (mRecipeType) {
                case None:
                    break;
                case Month:
                    mMonthRecipe = getIntent().getParcelableExtra(DATA_KEY);
                case Category:

                    break;
                case Keyword:
                    mKeyword = getIntent().getStringExtra(KEYWORD_KEY);
                    break;
                case Collect:
                    collectMode(true);
                    break;
                case Symptoms:
                    mKeyword = getIntent().getStringExtra(KEYWORD_KEY);
                    break;
                case Eattime:
                    mKeyword = getIntent().getStringExtra(KEYWORD_KEY);
                    break;
                case Type:
                    mKeyword = getIntent().getStringExtra(KEYWORD_KEY);
                    break;
                default:
                    break;
            }
        }
    }
    /**
     * 初始化零件
     */
    @Override
    protected void initWidget() {
        initLRecylerView();
    }
    /**
     * 初始化点击事件
     */
    @Override
    protected void initClick(){
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (RecipeKeywordListActivity.this.isVisible(RecipeKeywordListActivity.this.llBatchManagement)) //批量管理时，屏蔽点击事件
                    return;
                long recipeId = mRecipeListDataAdapter.getDataList().get(position).getId();
                RecipeDetailActivity.startActivity(RecipeKeywordListActivity.this,view,recipeId);
            }

        });

        mLRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                if (!RecipeKeywordListActivity.this.ismIsCollectModel()) {
                    return;
                }
                showBatchManagementLayout();
            }
        });
    }
    /**
     * 逻辑使用区
     */
    @Override
    protected void processLogic(){
        super.processLogic();
//        mPresenter.loadCategoryField(this.getContext());

        switch (mRecipeType) {
            case None:
                break;
            case Month:
                mPresenter.loadRecipeBeanbyMonth(this,mMonthRecipe.getKey());
            case Category:

                break;
            case Keyword:
                mPresenter.loadRecipeByKeyword(this,mKeyword);
                break;
            case Collect:
                mPresenter.loadCollectRecipe();
                break;
            case Symptoms:
                mPresenter.loadSymptomsRecipeBean(mKeyword);
                break;
            case Eattime:
                mPresenter.loadEattimeRecipeBean(mKeyword);
                break;
            case Type:
                mPresenter.loadTypeRecipeBean(mKeyword);
                break;
            default:
                break;
        }
    }

    private void initLRecylerView(){
        mRecipeListDataAdapter = new RecipeListDataAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mRecipeListDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //禁用下拉刷新功能
        mRecyclerView.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(false);

        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
    }

    @Override
    protected void initToolbar(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        switch (mRecipeType) {
            case None:
                break;
            case Month:
                mToolbarTitle.setText(mMonthRecipe.getTitle());
            case Category:

                break;
            case Keyword:
                mToolbarTitle.setText("关键字"+mKeyword+"的搜索结果");
                break;
            case Collect:
                mToolbarTitle.setText("我的收藏");
                break;
            case Symptoms:
                mToolbarTitle.setText(mKeyword + "食谱");
                break;
            case Eattime:
                mToolbarTitle.setText(mKeyword + "食谱");
                break;
            case Type:
                mToolbarTitle.setText(mKeyword);
                break;
            default:
                break;
        }


        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initSeachView();

    }

    void initSeachView(){
//        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setEllipsize(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Snackbar.make(findViewById(R.id.container), "Query: " + query, Snackbar.LENGTH_LONG)
//                        .show();
                RecipeKeywordListActivity.this.filterRecipe(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

//        searchView.closeSearch();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipelist_main, menu);

        MenuItem item = menu.findItem(R.id.recipelist_action_search);
        searchView.setMenuItem(item);



        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(mRecipeType==RECIPETYPE.Collect){
            this.processLogic();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void filterRecipe(String filterWord){
        if(mRecipeListDataAdapter.getDataList() != null && mRecipeListDataAdapter.getDataList().size()>0){
            List<RecipeBean> recipeBeans = mRecipeListDataAdapter.getDataList();
            List<Long> ids = new ArrayList<Long>();
            for (int i=0;i<recipeBeans.size();i++){
                ids.add(recipeBeans.get(i).getId());
            }
            mPresenter.filterByKeyword(this,ids,filterWord);
        }

    }

    @OnClick(R.id.tvSelectAll)
    public void onClickSelectAll() {
        Boolean isDealChk = false;
        String strShowText = "";
        if (tvSelectAll.getText().equals("全选")) {
            isDealChk = true;
            strShowText = "全不选";
        } else {
            isDealChk = false;
            strShowText = "全选";
        }
        for (RecipeBean bean : mRecipeListDataAdapter.getDataList()) {
            bean.setCanDelete(isDealChk);
        }
        mLRecyclerViewAdapter.notifyDataSetChanged();
        tvSelectAll.setText(strShowText);
    }

        @Override
    protected void onStop(){
        super.onStop();
        if(this.ismIsCollectModel()){
            cancelShowChk();
        }
    }

    @OnClick(R.id.tvDelete)
    public void onClickDelete() {
        showNormalDialog();

    }

    @OnClick(R.id.tvCancel)
    public void onClickCancel() {
//        for (RecipeBean bean : recipeListAdapter.recipes) {
//            bean.setShowCheckBox(false);
////            bean.isCanDelete(false);
//            bean.setIsCanDelete(false);
//        }
//        recipeListAdapter.notifyDataSetChanged();
//        hideBatchManagementLayout();
//        toggle.setDrawerIndicatorEnabled(true);
        cancelShowChk();
    }

    public void cancelShowChk(){
        for (RecipeBean bean : mRecipeListDataAdapter.getDataList()) {
            bean.setShowCheckBox(false);
            bean.setCanDelete(false);
        }
        mLRecyclerViewAdapter.notifyDataSetChanged();
        hideBatchManagementLayout();
    }

    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(RecipeKeywordListActivity.this);
//    normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("你确定要点删除这些吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
//                        recipeListAdapter.deleteRecipes();
                        RecipeKeywordListActivity.this.mPresenter.deleteCollectRecipe
                                (RecipeKeywordListActivity.this.mRecipeListDataAdapter.getDataList());
                        hideBatchManagementLayout();
                        dialog.dismiss();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do

                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();

    }

    /**
     * 显示批量管理布局
     */
    private void showBatchManagementLayout() {
        visible(llBatchManagement);
        for (RecipeBean bean : mRecipeListDataAdapter.getDataList()) {
            bean.showCheckBox = true;
        }
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * 显示批量管理布局
     */
    private void hideBatchManagementLayout() {
        gone(llBatchManagement);
        for (RecipeBean bean : mRecipeListDataAdapter.getDataList()) {
            bean.showCheckBox = false;
        }
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }


    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    private void collectMode(boolean isCollectModel) {
//        RecipeApplication.getApplication().isCollectMode = isCollectModel;
//        imvSearch.setVisibility(isCollectModel ? View.GONE : View.VISIBLE);
        mIsCollectModel = isCollectModel;
    }
    protected boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }
}
