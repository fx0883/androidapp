package com.ChildHealthDiet.app2.presenter;

import android.content.Context;

import com.ChildHealthDiet.app2.model.RecipeModelImpl;
import com.ChildHealthDiet.app2.model.bean.RecipeBean;
import com.ChildHealthDiet.app2.model.contract.RecipeModel;
import com.ChildHealthDiet.app2.presenter.contract.RecipeDetailContract;
import com.ChildHealthDiet.app2.ui.base.RxPresenter;
import com.ChildHealthDiet.app2.ui.categorys.RECIPETYPE;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RecipeDetailPresenter extends RxPresenter<RecipeDetailContract.View>
        implements RecipeDetailContract.Presenter {


    RECIPETYPE recipeType = RECIPETYPE.None;

    @Override
    public void getRecipeById(Context context,long id){
        Disposable disposable =
                Single.create(new SingleOnSubscribe<RecipeBean>() {
                    @Override
                    public void subscribe(SingleEmitter<RecipeBean> emitter) throws Exception {
                        RecipeModel recipeModelImpl = new RecipeModelImpl();
                        emitter.onSuccess(recipeModelImpl.getRecipeById(context,id));
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<RecipeBean>() {
                            @Override
                            public void accept(RecipeBean recipeBean) throws Exception {
                                RecipeDetailPresenter.this.getView().updateRecipe(recipeBean);
                            }
                        });
        addDisposable(disposable);
    }
    @Override
    public void updateRecipeBeanData(Context context,RecipeBean recipeBean){
        RecipeModel recipeModelImpl = new RecipeModelImpl();
        recipeModelImpl.updateRecipe(recipeBean);
    }

}
