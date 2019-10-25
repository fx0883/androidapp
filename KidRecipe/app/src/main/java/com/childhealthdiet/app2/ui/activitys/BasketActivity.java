package com.ChildHealthDiet.app2.ui.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ChildHealthDiet.app2.R;
import com.ChildHealthDiet.app2.adapter.RecipeBasketDataAdapter;
import com.ChildHealthDiet.app2.model.bean.RecipeBean;
import com.ChildHealthDiet.app2.presenter.BasketPresenter;
import com.ChildHealthDiet.app2.presenter.contract.BasketContract;
import com.ChildHealthDiet.app2.ui.base.BaseMVPActivity;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BasketActivity extends BaseMVPActivity<BasketContract.Presenter>
        implements BasketContract.View {

    @BindView(R.id.basket_recycler_view)
    LRecyclerView mRecyclerView;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private RecipeBasketDataAdapter mRecipeBasketDataAdapter = null;

    @BindView(R.id.basket_toolbar_id)
    Toolbar mToolbar;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, BasketActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected int getContentId() {
        return R.layout.activity_basket_recipe;
    }


    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }


    /**
     * 配置Toolbar
     *
     * @param toolbar
     */
    @Override
    protected void setUpToolbar(Toolbar toolbar) {

    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
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
    protected void initClick() {
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

        });
    }

    @Override
    protected BasketContract.Presenter bindPresenter() {
        return new BasketPresenter();
    }

    /**
     * 逻辑使用区
     */
    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.getBasketRecipeList();

    }

    private void initLRecylerView() {
        mRecipeBasketDataAdapter = new RecipeBasketDataAdapter(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mRecipeBasketDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //禁用下拉刷新功能
        mRecyclerView.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(false);

    }

    @Override
    protected void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void updateBasketRecipeList(List<RecipeBean> recipeBeans) {
        mRecipeBasketDataAdapter.setDataList(recipeBeans);
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.basket_clear_toolbar_id)
    protected void onClickClearButton() {

        List<RecipeBean> recipeBeans = mRecipeBasketDataAdapter.getDataList();
        if (recipeBeans == null || recipeBeans.size() == 0) {
            return;
        }
        RecipeBean[] aryRecipeBean = new RecipeBean[recipeBeans.size()];
        for (int i = 0; i < recipeBeans.size(); i++) {
            recipeBeans.get(i).setBasket(false);
            aryRecipeBean[i] = recipeBeans.get(i);
        }


        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(BasketActivity.this);
//    normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("你确定要点删除这些吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        BasketActivity.this.mPresenter.updateRecipes(aryRecipeBean);
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
//        normalDialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

        AlertDialog dialog = normalDialog.create();





        // 显示
        dialog.show();
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(this.getResources().getColor(R.color.colorPrimaryDark));
        dialog.setCanceledOnTouchOutside(false);//禁止点击 dialog 外部取消弹窗

    }


}
