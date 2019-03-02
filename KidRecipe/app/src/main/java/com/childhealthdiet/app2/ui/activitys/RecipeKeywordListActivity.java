package com.childhealthdiet.app2.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.childhealthdiet.app2.R;
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
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
                Toast.makeText(view.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                long recipeId = mRecipeListDataAdapter.getDataList().get(position).getId();
                RecipeDetailActivity.startActivity(RecipeKeywordListActivity.this,view,recipeId);
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
//            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//            if (matches != null && matches.size() > 0) {
//                String searchWrd = matches.get(0);
//                if (!TextUtils.isEmpty(searchWrd)) {
//                    searchView.setQuery(searchWrd, false);
//                }
//            }
//
//            return;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

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
}
