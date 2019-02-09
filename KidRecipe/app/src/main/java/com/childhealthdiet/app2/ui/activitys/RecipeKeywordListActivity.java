package com.childhealthdiet.app2.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
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

import java.util.List;

import butterknife.BindView;

public class RecipeKeywordListActivity extends BaseMVPActivity<RecipeKeywordListContract.Presenter>
        implements RecipeKeywordListContract.View {

    private static final String DATA_KEY = "data_Key";
    private static final String RECIPE_TYPE_KEY = "recipe_Type_Key";


    private MonthRecipe mMonthRecipe = null;
    private RECIPETYPE mRecipeType = RECIPETYPE.None;
    @BindView(R.id.recipelist_recycler_view)
    LRecyclerView mRecyclerView;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private RecipeListDataAdapter mRecipeListDataAdapter = null;

    @BindView(R.id.recipelist_toolbar_id)
    Toolbar mToolbar;
    @BindView(R.id.recipelist_toolbar_title_id)
    TextView mToolbarTitle;

    public static void startActivity(Context context, RECIPETYPE recipetype, MonthRecipe monthRecipe){
        Intent intent  =new Intent(context,RecipeKeywordListActivity.class);
        intent.putExtra(RECIPE_TYPE_KEY,recipetype.ordinal());
        intent.putExtra(DATA_KEY,monthRecipe);
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
        mToolbarTitle.setText(mMonthRecipe.getTitle());

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
