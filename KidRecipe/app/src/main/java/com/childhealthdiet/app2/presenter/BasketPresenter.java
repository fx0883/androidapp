package com.ChildHealthDiet.app2.presenter;

import com.ChildHealthDiet.app2.model.RecipeModelImpl;
import com.ChildHealthDiet.app2.model.bean.RecipeBean;
import com.ChildHealthDiet.app2.model.contract.RecipeModel;
import com.ChildHealthDiet.app2.presenter.contract.BasketContract;
import com.ChildHealthDiet.app2.ui.base.RxPresenter;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BasketPresenter extends RxPresenter<BasketContract.View>
        implements BasketContract.Presenter {

    @Override
    public void getBasketRecipeList() {
        Disposable disposable =
                Single.create(new SingleOnSubscribe<List<RecipeBean>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<RecipeBean>> emitter) throws Exception {
                        RecipeModel recipeModel = new RecipeModelImpl();
                        emitter.onSuccess(recipeModel.getBasketRecipeBean());
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<RecipeBean>>() {
                            @Override
                            public void accept(List<RecipeBean>  recipeBeans) throws Exception {
                                BasketPresenter.this.getView().updateBasketRecipeList(recipeBeans);
                            }
                        });
        addDisposable(disposable);
    }

    @Override
    public void updateRecipes(RecipeBean[] aryRecipeBean){
        Disposable disposable =
                Single.create(new SingleOnSubscribe<List<RecipeBean>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<RecipeBean>> emitter) throws Exception {
                        RecipeModel recipeModel = new RecipeModelImpl();
                        recipeModel.updateRecipes(aryRecipeBean);
                        emitter.onSuccess(recipeModel.getBasketRecipeBean());
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<RecipeBean>>() {
                            @Override
                            public void accept(List<RecipeBean>  recipeBeans) throws Exception {
                                BasketPresenter.this.getView().updateBasketRecipeList(recipeBeans);
                            }
                        });
        addDisposable(disposable);
    }
}
