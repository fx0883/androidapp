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

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

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


    @Override
    public List<RecipeBean> filterRecipeBeanbyKeyword(Context context,List<Long> ids,String keyword){
        RecipeBeanDao recipeBeanDao = DaoDbHelper.getInstance().getSession().getRecipeBeanDao();
        QueryBuilder qb = recipeBeanDao.queryBuilder();
        return qb.where(
                qb.and(RecipeBeanDao.Properties.Id.in(ids),RecipeBeanDao.Properties.Id.isNotNull()),
                qb.or(RecipeBeanDao.Properties.Name.like("%"+keyword+"%"),
                        RecipeBeanDao.Properties.Ingredients.like("%"+keyword+"%"),
                        RecipeBeanDao.Properties.Eattime.like("%"+keyword+"%"),
                        RecipeBeanDao.Properties.Prompt.like("%"+keyword+"%"),
                        RecipeBeanDao.Properties.Type.like("%"+keyword+"%"),
                        RecipeBeanDao.Properties.Practice.like("%"+keyword+"%"),
                        RecipeBeanDao.Properties.Material.like("%"+keyword+"%"))
        ).list();
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

    @Override
    public void updateRecipe(RecipeBean recipeBean){
        RecipeBeanDao recipeBeanDao = DaoDbHelper.getInstance().getSession().getRecipeBeanDao();
        recipeBeanDao.update(recipeBean);
    }
    @Override
    public void updateRecipes(RecipeBean[] recipeBeans){
        RecipeBeanDao recipeBeanDao = DaoDbHelper.getInstance().getSession().getRecipeBeanDao();
        recipeBeanDao.updateInTx(recipeBeans);
    }


    @Override
    public List<RecipeBean> getBasketRecipeBean(){
        RecipeBeanDao recipeBeanDao = DaoDbHelper.getInstance().getSession().getRecipeBeanDao();
        QueryBuilder qb = recipeBeanDao.queryBuilder();
        return qb.where(RecipeBeanDao.Properties.Basket.eq(true)).list();
    }
    @Override
    public List<RecipeBean> getColletRecipeBean(){
        RecipeBeanDao recipeBeanDao = DaoDbHelper.getInstance().getSession().getRecipeBeanDao();
        QueryBuilder qb = recipeBeanDao.queryBuilder();
        return qb.where(RecipeBeanDao.Properties.Collect.eq(true)).list();
    }
    @Override
    public void deleteCollectRecipe(List<RecipeBean> recipeBeans) {
        if(recipeBeans != null){
            RecipeBeanDao recipeBeanDao = DaoDbHelper.getInstance().getSession().getRecipeBeanDao();
            for(RecipeBean itemRecipe : recipeBeans) {//其内部实质上还是调用了迭代器遍历方式，这种循环方式还有其他限制，不建议使用。
                if(itemRecipe.isCanDelete){
                    itemRecipe.setCollect(false);
                    itemRecipe.setShowCheckBox(false);
                    recipeBeanDao.update(itemRecipe);
                }
            }
        }
    }
}
