package com.childhealthdiet.app2.model;

import android.content.Context;

import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.model.bean.MonthRecipe;
import com.childhealthdiet.app2.model.bean.RecipeBean;
import com.childhealthdiet.app2.model.bean.RecipeBeanDao;
import com.childhealthdiet.app2.model.contract.MonthRecipeModel;
import com.childhealthdiet.app2.model.contract.RecipeModel;
import com.childhealthdiet.app2.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class RecipeModelImpl implements RecipeModel {

    @Override
    public List<RecipeBean> loadRecipeBeanbyKeywords(Context context, String[] keywords){
        return null;
    }

    @Override
    public List<RecipeBean> searchRecipeBeanbyKeyword(Context context,String keyword){
        RecipeBeanDao recipeBeanDao = DaoDbHelper.getInstance().getSession().getRecipeBeanDao();
        return recipeBeanDao.queryBuilder().whereOr(RecipeBeanDao.Properties.Name.like("%"+keyword+"%"),
                RecipeBeanDao.Properties.Ingredients.like("%"+keyword+"%"),
                RecipeBeanDao.Properties.Eattime.like("%"+keyword+"%"),
                RecipeBeanDao.Properties.Prompt.like("%"+keyword+"%"),
                RecipeBeanDao.Properties.Type.like("%"+keyword+"%"),
                RecipeBeanDao.Properties.Practice.like("%"+keyword+"%"),
                RecipeBeanDao.Properties.Material.like("%"+keyword+"%")).list();
    }

//    RecipeBeanDao.Properties.Month.eq(hashMapCondition.get("value"))
    @Override
    public List<RecipeBean> loadRecipeBeanbyMonth(Context context,String keyword){
        RecipeBeanDao recipeBeanDao = DaoDbHelper.getInstance().getSession().getRecipeBeanDao();
        return recipeBeanDao.queryBuilder().where(RecipeBeanDao.Properties.Month.eq(keyword)).list();
    }


    @Override
    public RecipeBean getRecipeById(Context context,long id){
        RecipeBeanDao recipeBeanDao = DaoDbHelper.getInstance().getSession().getRecipeBeanDao();
//        List<RecipeBean> recipeBeans = recipeBeanDao.queryBuilder().where(RecipeBeanDao.Properties.Id.eq(strID)).list();
        return recipeBeanDao.load(id);

    }
}
