package com.childhealthdiet.app2.presenter.contract;

import android.content.Context;

import com.childhealthdiet.app2.model.bean.MineItem;
import com.childhealthdiet.app2.model.bean.RecipeCategory;
import com.childhealthdiet.app2.ui.base.BaseContract;

import java.util.List;

public interface MineContract {

    interface View extends BaseContract.BaseView{
        void updateMineItem(List<MineItem> mineItems);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void getMineItems(Context context);
    }
}
