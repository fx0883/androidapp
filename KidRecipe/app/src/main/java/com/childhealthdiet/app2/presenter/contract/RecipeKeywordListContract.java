package com.childhealthdiet.app2.presenter.contract;

import android.content.Context;

import com.childhealthdiet.app2.model.bean.MonthRecipe;
import com.childhealthdiet.app2.model.bean.RecipeBean;
import com.childhealthdiet.app2.ui.base.BaseContract;

import java.util.List;

public interface RecipeKeywordListContract {

    interface View extends BaseContract.BaseView{
        void updateRecipe(List<RecipeBean> recipeBeans);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void loadRecipeBeanbyMonth(Context context,String strKey);
        void loadRecipeByKeyword(Context context,String strKey);
        void filterByKeyword(Context context,List<Long> ids,String strKey);
        void loadCollectRecipe();
        void deleteCollectRecipe(List<RecipeBean> recipeBeans);
    }
}
