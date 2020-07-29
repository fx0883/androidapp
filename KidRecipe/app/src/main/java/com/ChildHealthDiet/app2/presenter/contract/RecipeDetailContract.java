package com.ChildHealthDiet.app2.presenter.contract;

import android.content.Context;

import com.ChildHealthDiet.app2.model.bean.RecipeBean;
import com.ChildHealthDiet.app2.ui.base.BaseContract;

public interface RecipeDetailContract {

    interface View extends BaseContract.BaseView{
//        void finishRefresh(BillboardPackage beans);
        void updateRecipe(RecipeBean recipeBean);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void getRecipeById(Context context,long id);
        void updateRecipeBeanData(Context context,RecipeBean recipeBean);

    }
}
