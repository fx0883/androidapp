package com.ChildHealthDiet.app2.presenter;

import android.content.Context;

import com.ChildHealthDiet.app2.context.Kidinfo;
import com.ChildHealthDiet.app2.context.UserContext;
import com.ChildHealthDiet.app2.model.MonthRecipeModelImpl;
import com.ChildHealthDiet.app2.model.RecipeModelImpl;
import com.ChildHealthDiet.app2.model.bean.MonthRecipe;
import com.ChildHealthDiet.app2.model.bean.RecipeBean;
import com.ChildHealthDiet.app2.model.contract.MonthRecipeModel;
import com.ChildHealthDiet.app2.model.contract.RecipeModel;
import com.ChildHealthDiet.app2.presenter.contract.RecipeKeywordListContract;
import com.ChildHealthDiet.app2.ui.base.RxPresenter;
import com.ChildHealthDiet.app2.ui.categorys.RECIPETYPE;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RecipeKeywordListPresenter extends RxPresenter<RecipeKeywordListContract.View>
        implements RecipeKeywordListContract.Presenter {


    RECIPETYPE recipeType = RECIPETYPE.None;

    @Override
    public void loadRecipeBeanbyMonth(Context context,String strKey){
        Disposable disposable =
                Single.create(new SingleOnSubscribe<List<RecipeBean>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<RecipeBean>> emitter) throws Exception {
                        RecipeModel recipeModelImpl = new RecipeModelImpl();
                        emitter.onSuccess(recipeModelImpl.loadRecipeBeanbyMonth(context,strKey));
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<RecipeBean>>() {
                            @Override
                            public void accept(List<RecipeBean> recipeBeans) throws Exception {
                                RecipeKeywordListPresenter.this.getView().updateRecipe(recipeBeans);
                            }
                        });
        addDisposable(disposable);
    }

    @Override
    public void loadRecipeByKeyword(Context context,String strKey) {
        Disposable disposable =
                Single.create(new SingleOnSubscribe<List<RecipeBean>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<RecipeBean>> emitter) throws Exception {
                        RecipeModel recipeModel = new RecipeModelImpl();
                        List<RecipeBean> recipeBeans = recipeModel.searchRecipeBeanbyKeyword(context,strKey);
                        emitter.onSuccess(recipeBeans);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<RecipeBean>>() {
                            @Override
                            public void accept(List<RecipeBean> recipeBeans) throws Exception {
                                RecipeKeywordListPresenter.this.getView().updateRecipe(recipeBeans);
                            }
                        });
        addDisposable(disposable);
    }
    @Override
    public void filterByKeyword(Context context,List<Long> ids,String strKey){
        Disposable disposable =
                Single.create(new SingleOnSubscribe<List<RecipeBean>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<RecipeBean>> emitter) throws Exception {
                        RecipeModel recipeModel = new RecipeModelImpl();
                        List<RecipeBean> recipeBeans = recipeModel.filterRecipeBeanbyKeyword(context,ids,strKey);
                        emitter.onSuccess(recipeBeans);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<RecipeBean>>() {
                            @Override
                            public void accept(List<RecipeBean> recipeBeans) throws Exception {
                                RecipeKeywordListPresenter.this.getView().updateRecipe(recipeBeans);
                            }
                        });
        addDisposable(disposable);
    }
    @Override
    public void loadCollectRecipe(){
        Disposable disposable =
                Single.create(new SingleOnSubscribe<List<RecipeBean>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<RecipeBean>> emitter) throws Exception {
                        RecipeModel recipeModel = new RecipeModelImpl();
                        List<RecipeBean> recipeBeans = recipeModel.getColletRecipeBean();
                        emitter.onSuccess(recipeBeans);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<RecipeBean>>() {
                            @Override
                            public void accept(List<RecipeBean> recipeBeans) throws Exception {
                                RecipeKeywordListPresenter.this.getView().updateRecipe(recipeBeans);
                            }
                        });
        addDisposable(disposable);
    }

    @Override
    public void deleteCollectRecipe(List<RecipeBean> recipeBeans){
        Disposable disposable =
                Single.create(new SingleOnSubscribe<List<RecipeBean>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<RecipeBean>> emitter) throws Exception {
                        RecipeModel recipeModel = new RecipeModelImpl();
                        recipeModel.deleteCollectRecipe(recipeBeans);
                        List<RecipeBean> recipeBeans2 = recipeModel.getColletRecipeBean();
                        emitter.onSuccess(recipeBeans2);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<RecipeBean>>() {
                            @Override
                            public void accept(List<RecipeBean> recipeBeans2) throws Exception {
                                RecipeKeywordListPresenter.this.getView().updateRecipe(recipeBeans2);
                            }
                        });
        addDisposable(disposable);
    }


    @Override
    public void loadSymptomsRecipeBean(String strSymptoms){
        Disposable disposable =
                Single.create(new SingleOnSubscribe<List<RecipeBean>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<RecipeBean>> emitter) throws Exception {
                        RecipeModel recipeModel = new RecipeModelImpl();
                        List<RecipeBean> recipeBeans = recipeModel.getSymptomsRecipeBean(strSymptoms);
                        emitter.onSuccess(recipeBeans);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<RecipeBean>>() {
                            @Override
                            public void accept(List<RecipeBean> recipeBeans) throws Exception {
                                RecipeKeywordListPresenter.this.getView().updateRecipe(recipeBeans);
                            }
                        });
        addDisposable(disposable);
    }


    private String getMonthKey(Context context,int intMonth){
        String strRet = "";
        MonthRecipeModel monthRecipeModel = new MonthRecipeModelImpl();

        List<MonthRecipe> monthRecipes = monthRecipeModel.loadMonthRecipe(context);
        for (int i=0;i<monthRecipes.size();i++){
            MonthRecipe itemMonthRecipe = monthRecipes.get(i);
//            int intmonth = this.convertStrtoint(strMonth);
            if(intMonth < 4 || intMonth > 72){
                break;
            }
            if(itemMonthRecipe.getKey().indexOf("-")>0){
                String startMonth = itemMonthRecipe.getKey().split("-")[0];
                String endMonth = itemMonthRecipe.getKey().split("-")[1];

                int intStartMonth = this.convertStrtoint(startMonth);
                int intEndMonth = this.convertStrtoint(endMonth);
                if(intMonth>=intStartMonth && intMonth<=intEndMonth){
                    strRet = itemMonthRecipe.getKey();
                    break;
                }
            }
            else{
                String curMonth = itemMonthRecipe.getKey();
                int intcurMonth = this.convertStrtoint(curMonth);
                if(intMonth == intcurMonth){
                    strRet = itemMonthRecipe.getKey();
                    break;
                }
            }
        }
        return  strRet;
    }

    private int convertStrtoint(String strValue){
        int b=0;
        try {
            b = Integer.valueOf(strValue).intValue();
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return b;
    }


    @Override
    public void loadEattimeRecipeBean(Context context,String strEatTime){
        Kidinfo kidinfo = UserContext.getInstance().getmKidinfo(context);
        int intMonth =0;
        try{
            intMonth = kidinfo.getMonthAge();
        }
        catch (Exception e){

        }

        String strMonthKey = this.getMonthKey(context,intMonth);

        Disposable disposable =
                Single.create(new SingleOnSubscribe<List<RecipeBean>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<RecipeBean>> emitter) throws Exception {
                        RecipeModel recipeModel = new RecipeModelImpl();
                        List<RecipeBean> recipeBeans = recipeModel.getEattimeRecipeBean(strEatTime,strMonthKey);
                        emitter.onSuccess(recipeBeans);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<RecipeBean>>() {
                            @Override
                            public void accept(List<RecipeBean> recipeBeans) throws Exception {
                                RecipeKeywordListPresenter.this.getView().updateRecipe(recipeBeans);
                            }
                        });
        addDisposable(disposable);
    }


    @Override
    public void loadTypeRecipeBean(String strType){
        Disposable disposable =
                Single.create(new SingleOnSubscribe<List<RecipeBean>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<RecipeBean>> emitter) throws Exception {
                        RecipeModel recipeModel = new RecipeModelImpl();
                        List<RecipeBean> recipeBeans = recipeModel.getTypeRecipeBean(strType);
                        emitter.onSuccess(recipeBeans);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<RecipeBean>>() {
                            @Override
                            public void accept(List<RecipeBean> recipeBeans) throws Exception {
                                RecipeKeywordListPresenter.this.getView().updateRecipe(recipeBeans);
                            }
                        });
        addDisposable(disposable);
    }

    @Override
    public void loadIngredientsRecipeBean(String strIngredients){
        Disposable disposable =
                Single.create(new SingleOnSubscribe<List<RecipeBean>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<RecipeBean>> emitter) throws Exception {
                        RecipeModel recipeModel = new RecipeModelImpl();
                        List<RecipeBean> recipeBeans = recipeModel.getIngredientsRecipeBean(strIngredients);
                        emitter.onSuccess(recipeBeans);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<RecipeBean>>() {
                            @Override
                            public void accept(List<RecipeBean> recipeBeans) throws Exception {
                                RecipeKeywordListPresenter.this.getView().updateRecipe(recipeBeans);
                            }
                        });
        addDisposable(disposable);
    }
}
