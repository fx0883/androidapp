package com.childhealthdiet.app2.presenter.contract;

import android.content.Context;

import com.childhealthdiet.app2.model.bean.MonthRecipe;
import com.childhealthdiet.app2.ui.base.BaseContract;

import java.util.List;

public interface FragmentHomeContract {

    interface View extends BaseContract.BaseView{
//        void finishRefresh(BillboardPackage beans);
        void updateCategory(String[] aryCategory);
        void updateMonthRecipeMenu(List<MonthRecipe> monthRecipes);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void loadMonthRecipe(Context context);
        void loadCategoryField(Context context);
    }
}
