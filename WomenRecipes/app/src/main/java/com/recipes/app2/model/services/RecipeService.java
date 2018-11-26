package com.recipes.app2.model.services;

import android.os.SystemClock;

import com.recipes.app2.RecipeApplication;
import com.recipes.app2.model.bean.DaoSession;
import com.recipes.app2.model.bean.RecipeBean;
import com.recipes.app2.model.bean.RecipeBeanDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

import com.recipes.app2.R;

public class RecipeService {

    public int curClickId;
    public List<RecipeBean> recipeBeans = null;

    private static RecipeService instance = null;
//    public RecipeService(){
//        instance = this;
//    }

    public static RecipeService getInstance(){
        if(instance == null)
        {
            instance = new RecipeService();
        }
        return instance;

    }

    public void updateRecipe(RecipeBean recipeBean){
        if(recipeBeans != null){
            for(RecipeBean itemRecipe : recipeBeans) {//其内部实质上还是调用了迭代器遍历方式，这种循环方式还有其他限制，不建议使用。
                if(itemRecipe.getId().longValue() == recipeBean.getId().longValue()){
                    itemRecipe.setCollect(recipeBean.getCollect());
                    RecipeApplication.getApplication().daoSession.getRecipeBeanDao().update(itemRecipe);
                }
            }
        }
    }




    public Observable<List<RecipeBean>> getRecipeObservable() {
        return Observable.defer(new Callable<ObservableSource<? extends List<RecipeBean>>>() {
            @Override public ObservableSource<? extends List<RecipeBean>> call() throws Exception {
                // Do some long running operation
                DaoSession daoSession = RecipeApplication.getApplication().daoSession;
                RecipeBeanDao recipeBeanDao = daoSession.getRecipeBeanDao();
//                List<RecipeBean> recipeBeans= recipeBeanDao.loadAll();
                RecipeService.getInstance().recipeBeans= recipeBeanDao.queryBuilder().where(RecipeBeanDao.Properties.Month.eq("1")).list();
                return Observable.just(RecipeService.getInstance().recipeBeans);
            }
        });
    }

    public Observable<List<RecipeBean>> searchRecipeObservable(final String key) {
        return Observable.defer(new Callable<ObservableSource<? extends List<RecipeBean>>>() {
            @Override public ObservableSource<? extends List<RecipeBean>> call() throws Exception {
                // Do some long running operation
                DaoSession daoSession = RecipeApplication.getApplication().daoSession;
                RecipeBeanDao recipeBeanDao = daoSession.getRecipeBeanDao();
//                List<RecipeBean> recipeBeans= recipeBeanDao.loadAll();
                RecipeService.getInstance().recipeBeans=
                        recipeBeanDao.queryBuilder().whereOr(RecipeBeanDao.Properties.Name.like("%"+key+"%"),RecipeBeanDao.Properties.Ingredients.like("%"+key+"%")).list();
                return Observable.just(RecipeService.getInstance().recipeBeans);
            }
        });
    }

    public Observable<List<RecipeBean>> getRecipeObservable(int id) {
        this.curClickId = id;
        return Observable.defer(new Callable<ObservableSource<? extends List<RecipeBean>>>() {
            @Override public ObservableSource<? extends List<RecipeBean>> call() throws Exception {
                // Do some long running operation
                DaoSession daoSession = RecipeApplication.getApplication().daoSession;
                RecipeBeanDao recipeBeanDao = daoSession.getRecipeBeanDao();
//                List<RecipeBean> recipeBeans= recipeBeanDao.loadAll();

                HashMap<String,String> hashMapCondition = RecipeService.getInstance().getRecipeCondition(RecipeService.getInstance().curClickId);


//                List<RecipeBean> recipeBeans = null;
                if(hashMapCondition.get("key").equals("month"))
                {
                    RecipeService.getInstance().recipeBeans = recipeBeanDao.queryBuilder().where(RecipeBeanDao.Properties.Month.eq(hashMapCondition.get("value"))).list();
                }
                else if(hashMapCondition.get("key").equals("type")){
                    RecipeService.getInstance().recipeBeans = recipeBeanDao.queryBuilder().where(RecipeBeanDao.Properties.Type.eq(hashMapCondition.get("value"))).list();
                }
                else if(hashMapCondition.get("key").equals("collect")){
                    RecipeService.getInstance().recipeBeans = recipeBeanDao.queryBuilder().where(RecipeBeanDao.Properties.Collect.eq(true)).list();
                }


                return Observable.just(RecipeService.getInstance().recipeBeans);
            }
        });
    }

    private static final String keymonth = "month";
    private static final String keytype = "type";
    public HashMap<String,String> getRecipeCondition(int id){
        HashMap<String,String> retHashmap = new HashMap<String, String>();
        switch (id)
        {
            case R.id.nav_one_month:
                retHashmap.put("key",keymonth);
                retHashmap.put("value","1");
                break;
            case R.id.nav_two_month:
                retHashmap.put("key",keymonth);
                retHashmap.put("value","2");
                break;
            case R.id.nav_three_month:
                retHashmap.put("key",keymonth);
                retHashmap.put("value","3");
                break;
            case R.id.nav_four_month:
                retHashmap.put("key",keymonth);
                retHashmap.put("value","4");
                break;
            case R.id.nav_five_month:
                retHashmap.put("key",keymonth);
                retHashmap.put("value","5");
                break;
            case R.id.nav_six_month:
                retHashmap.put("key",keymonth);
                retHashmap.put("value","6");
                break;
            case R.id.nav_seven_month:
                retHashmap.put("key",keymonth);
                retHashmap.put("value","7");
                break;
            case R.id.nav_eight_month:
                retHashmap.put("key",keymonth);
                retHashmap.put("value","8");
                break;
            case R.id.nav_nine_month:
                retHashmap.put("key",keymonth);
                retHashmap.put("value","9");
                break;
            case R.id.nav_ten_month:
                retHashmap.put("key",keymonth);
                retHashmap.put("value","10");
                break;


            case R.id.nav_morning_sickness_recipe:
                retHashmap.put("key",keytype);
                retHashmap.put("value","缓解孕吐食谱");
                break;
            case R.id.nav_enrichtheblood_recipe:
                retHashmap.put("key",keytype);
                retHashmap.put("value","补血食谱");
                break;
            case R.id.nav_vitamin_recipe:
                retHashmap.put("key",keytype);
                retHashmap.put("value","孕妇补维生素食谱");
                break;
            case R.id.nav_advantage_recipe:
                retHashmap.put("key",keytype);
                retHashmap.put("value","优生食谱");
                break;

            case R.id.nav_collect:
                retHashmap.put("key","collect");
                retHashmap.put("value","collect");
                break;

        }

        return retHashmap;
    }

}
