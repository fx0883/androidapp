package com.childhealthdiet.app2.model.contract;

import android.content.Context;

import com.childhealthdiet.app2.model.bean.RecipeBean;

import java.util.List;

public interface RecipeModel {
    List<RecipeBean> loadRecipeBeanbyKeywords(Context context,String[] keywords);
    List<RecipeBean> searchRecipeBeanbyKeyword(Context context,String keyword);
    List<RecipeBean> loadRecipeBeanbyMonth(Context context,String keyword);

    RecipeBean getRecipeById(Context context,long id);
}
