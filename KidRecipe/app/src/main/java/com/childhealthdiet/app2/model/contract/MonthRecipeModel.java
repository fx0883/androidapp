package com.childhealthdiet.app2.model.contract;

import android.content.Context;

import com.childhealthdiet.app2.model.bean.MonthRecipe;

import java.util.List;

public interface MonthRecipeModel {
    List<MonthRecipe> loadMonthRecipe(Context context);
}

