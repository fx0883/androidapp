package com.childhealthdiet.app2.presenter.contract;

import android.content.Context;

import com.childhealthdiet.app2.model.bean.MonthRecipe;
import com.childhealthdiet.app2.model.bean.RecipeBean;
import com.childhealthdiet.app2.ui.base.BaseContract;

import java.util.List;

public interface RecipeDetailContract {

    interface View extends BaseContract.BaseView{
//        void finishRefresh(BillboardPackage beans);
        void updateRecipe(RecipeBean recipeBean);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void getRecipeById(Context context,long id);
    }
}
