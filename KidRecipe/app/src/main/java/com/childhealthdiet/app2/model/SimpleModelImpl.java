package com.childhealthdiet.app2.model;

import android.content.Context;
import android.content.res.TypedArray;

import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.model.bean.MineItem;
import com.childhealthdiet.app2.model.bean.MonthRecipe;
import com.childhealthdiet.app2.model.bean.RecipeBean;
import com.childhealthdiet.app2.model.bean.RecipeCategory;
import com.childhealthdiet.app2.model.contract.SimpleModel;
import com.childhealthdiet.app2.ui.base.BaseModel;
import com.childhealthdiet.app2.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleModelImpl implements SimpleModel {

    @Override
    public String[] loadCategoryField(Context context) {
        String[] gvCategorys = context.getResources().getStringArray(R.array.gv_category);
        return gvCategorys;
    }
    @Override
    public List<String> loadFieldByKey(Context context, int intKey){
        String[] strs = context.getResources().getStringArray(intKey);
        return Arrays.asList(strs);
    }

    @Override
    public List<RecipeCategory> loadCategoryslist(Context context){
        String jsonFilePath = context.getString(R.string.category_json_path);
        String monthJson = FileUtils.getJson(context,jsonFilePath);
        Gson gson = new Gson();
        List<RecipeCategory> categoryList = (List<RecipeCategory>)gson.fromJson(monthJson,
                new TypeToken<List<RecipeCategory>>(){}.getType());
        return categoryList;
    }


    @Override
    public List<MineItem> loadMineitem(Context context){
        String jsonFilePath = context.getString(R.string.mineitem_json_path);
        String monthJson = FileUtils.getJson(context,jsonFilePath);
        Gson gson = new Gson();
        List<MineItem> mineItemList = (List<MineItem>)gson.fromJson(monthJson,
                new TypeToken<List<MineItem>>(){}.getType());
        return mineItemList;
    }




}
