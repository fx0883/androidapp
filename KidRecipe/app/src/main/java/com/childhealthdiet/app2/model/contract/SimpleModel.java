package com.ChildHealthDiet.app2.model.contract;

import android.content.Context;

import com.ChildHealthDiet.app2.model.bean.MineItem;
import com.ChildHealthDiet.app2.model.bean.RecipeCategory;

import java.util.List;

public interface SimpleModel {
    String[] loadCategoryField(Context context);
    List<String> loadFieldByKey(Context context, int intKey);
    List<RecipeCategory> loadCategoryslist(Context context);
    List<MineItem> loadMineitem(Context context);
}
