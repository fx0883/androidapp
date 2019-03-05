package com.childhealthdiet.app2.presenter;

import android.content.Context;

import com.childhealthdiet.app2.model.RecipeModelImpl;
import com.childhealthdiet.app2.model.SimpleModelImpl;
import com.childhealthdiet.app2.model.bean.RecipeBean;
import com.childhealthdiet.app2.model.contract.RecipeModel;
import com.childhealthdiet.app2.model.contract.SimpleModel;
import com.childhealthdiet.app2.presenter.contract.RecipeKeywordListContract;
import com.childhealthdiet.app2.ui.base.RxPresenter;
import com.childhealthdiet.app2.ui.categorys.RECIPETYPE;

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


    @Override
    public void loadEattimeRecipeBean(String strEatTime){
        Disposable disposable =
                Single.create(new SingleOnSubscribe<List<RecipeBean>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<RecipeBean>> emitter) throws Exception {
                        RecipeModel recipeModel = new RecipeModelImpl();
                        List<RecipeBean> recipeBeans = recipeModel.getEattimeRecipeBean(strEatTime);
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
}
