package com.childhealthdiet.app2.model.contract;

import android.content.Context;

import com.childhealthdiet.app2.model.bean.MineItem;
import com.childhealthdiet.app2.model.bean.RecipeCategory;

import java.util.List;
import java.util.Map;

public interface SimpleModel {
    String[] loadCategoryField(Context context);
    List<String> loadFieldByKey(Context context, int intKey);
    List<RecipeCategory> loadCategoryslist(Context context);
    List<MineItem> loadMineitem(Context context);
}
