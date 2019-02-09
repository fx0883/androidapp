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
//                        SimpleModelImpl simpleModel = new SimpleModelImpl();
//                        MonthRecipeModel monthRecipeModelImpl = new MonthRecipeModelImpl();
//                        emitter.onSuccess(monthRecipeModelImpl.loadMonthRecipe(context));

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
                Single.create(new SingleOnSubscribe<String[]>() {
                    @Override
                    public void subscribe(SingleEmitter<String[]> emitter) throws Exception {
                        SimpleModel simpleModel = new SimpleModelImpl();
                        emitter.onSuccess(simpleModel.loadCategoryField(context));
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String[]>() {
                            @Override
                            public void accept(String[] strCategorys) throws Exception {
//                                RecipeKeywordListPresenter.this.getView().updateCategory(strCategorys);
                            }
                        });
        addDisposable(disposable);
    }
}
