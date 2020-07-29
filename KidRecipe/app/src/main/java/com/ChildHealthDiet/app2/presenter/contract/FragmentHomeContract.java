package com.ChildHealthDiet.app2.presenter.contract;

import android.content.Context;

import com.ChildHealthDiet.app2.model.bean.MonthRecipe;
import com.ChildHealthDiet.app2.ui.base.BaseContract;

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
