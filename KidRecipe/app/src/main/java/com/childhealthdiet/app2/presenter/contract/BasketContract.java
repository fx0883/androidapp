package com.childhealthdiet.app2.presenter.contract;

import android.content.Context;

import com.childhealthdiet.app2.model.bean.RecipeBean;
import com.childhealthdiet.app2.model.bean.RecipeCategory;
import com.childhealthdiet.app2.ui.base.BaseContract;

import java.util.List;

public interface BasketContract {

    interface View extends BaseContract.BaseView{
        void updateBasketRecipeList(List<RecipeBean> recipeBeans);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void getBasketRecipeList();
        void updateRecipes(RecipeBean[] aryRecipeBean);
    }
}
