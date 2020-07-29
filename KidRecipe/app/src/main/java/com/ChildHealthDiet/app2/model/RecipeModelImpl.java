package com.ChildHealthDiet.app2.model;

import android.content.Context;

import com.ChildHealthDiet.app2.model.bean.RecipeBean;
import com.ChildHealthDiet.app2.model.bean.RecipeBeanDao;
import com.ChildHealthDiet.app2.model.contract.RecipeModel;

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
                RecipeBeanDao.Properties.Material.like("%"+keyword+"%"),
                RecipeBeanDao.Properties.Symptoms.like("%"+keyword+"%"))
                .orderAsc(RecipeBeanDao.Properties.Month).list();
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
                        RecipeBeanDao.Properties.Material.like("%"+keyword+"%"),
                        RecipeBeanDao.Properties.Symptoms.like("%"+keyword+"%"))
        ).orderAsc(RecipeBeanDao.Properties.Month).list();
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
        return qb.where(RecipeBeanDao.Properties.Basket.eq(true)).orderAsc(RecipeBeanDao.Properties.Month).list();
    }
    @Override
    public List<RecipeBean> getColletRecipeBean(){
        RecipeBeanDao recipeBeanDao = DaoDbHelper.getInstance().getSession().getRecipeBeanDao();
        QueryBuilder qb = recipeBeanDao.queryBuilder();
        return qb.where(RecipeBeanDao.Properties.Collect.eq(true)).orderAsc(RecipeBeanDao.Properties.Month).list();
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

    @Override
    public List<RecipeBean> getSymptomsRecipeBean(String strSymptoms){
        RecipeBeanDao recipeBeanDao = DaoDbHelper.getInstance().getSession().getRecipeBeanDao();
        QueryBuilder qb = recipeBeanDao.queryBuilder();
        return qb.where(RecipeBeanDao.Properties.Symptoms.like("%"+strSymptoms+"%")).orderAsc(RecipeBeanDao.Properties.Month).list();
    }


    @Override
    public List<RecipeBean> getEattimeRecipeBean(String strEatTime,String strMonth){
        RecipeBeanDao recipeBeanDao = DaoDbHelper.getInstance().getSession().getRecipeBeanDao();
        QueryBuilder qb = recipeBeanDao.queryBuilder();
        if(strMonth.equals("")){
            return qb.where(RecipeBeanDao.Properties.Eattime.like("%"+strEatTime+"%"))
                    .orderAsc(RecipeBeanDao.Properties.Month).list();
        }
        else {
            return qb.where(
                    qb.and(RecipeBeanDao.Properties.Month.eq(strMonth),
                            RecipeBeanDao.Properties.Eattime.like("%"+strEatTime+"%")))
                            .orderAsc(RecipeBeanDao.Properties.Month).list();
        }

    }

    @Override
    public List<RecipeBean> getTypeRecipeBean(String strTypeName){
        RecipeBeanDao recipeBeanDao = DaoDbHelper.getInstance().getSession().getRecipeBeanDao();
        QueryBuilder qb = recipeBeanDao.queryBuilder();
        return qb.where(RecipeBeanDao.Properties.Type.like("%"+strTypeName+"%")).orderAsc(RecipeBeanDao.Properties.Month).list();
    }

    @Override
    public List<RecipeBean> getIngredientsRecipeBean(String strIngredients){
        RecipeBeanDao recipeBeanDao = DaoDbHelper.getInstance().getSession().getRecipeBeanDao();
        QueryBuilder qb = recipeBeanDao.queryBuilder();
        return qb.where(RecipeBeanDao.Properties.Ingredients.like("%"+strIngredients+"%"))
                .orderAsc(RecipeBeanDao.Properties.Month).list();
    }
}
