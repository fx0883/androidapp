package com.ChildHealthDiet.app2.model;

import android.content.Context;

import com.ChildHealthDiet.app2.R;
import com.ChildHealthDiet.app2.model.bean.MonthRecipe;
import com.ChildHealthDiet.app2.model.contract.MonthRecipeModel;
import com.ChildHealthDiet.app2.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class MonthRecipeModelImpl implements MonthRecipeModel {

    @Override
    public List<MonthRecipe> loadMonthRecipe(Context context) {
        String jsonFilePath = context.getString(R.string.month_json_path);
        String monthJson = FileUtils.getJson(context,jsonFilePath);
        Gson gson = new Gson();
        List<MonthRecipe> monthRecipeList = (List<MonthRecipe>)gson .fromJson(monthJson,
                new TypeToken<List<MonthRecipe>>(){}.getType());
        return monthRecipeList;
    }

}
