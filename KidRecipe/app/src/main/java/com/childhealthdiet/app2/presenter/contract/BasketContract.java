package com.ChildHealthDiet.app2.presenter.contract;

import com.ChildHealthDiet.app2.model.bean.RecipeBean;
import com.ChildHealthDiet.app2.ui.base.BaseContract;

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
