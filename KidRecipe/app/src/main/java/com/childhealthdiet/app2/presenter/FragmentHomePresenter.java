package com.childhealthdiet.app2.presenter;

import android.content.Context;

import com.childhealthdiet.app2.model.MonthRecipeModelImpl;
import com.childhealthdiet.app2.model.SimpleModelImpl;
import com.childhealthdiet.app2.model.bean.MonthRecipe;
import com.childhealthdiet.app2.model.contract.MonthRecipeModel;
import com.childhealthdiet.app2.model.contract.SimpleModel;
import com.childhealthdiet.app2.presenter.contract.FragmentHomeContract;
import com.childhealthdiet.app2.ui.base.BaseMVPFragment;
import com.childhealthdiet.app2.ui.base.RxPresenter;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FragmentHomePresenter extends RxPresenter<FragmentHomeContract.View>
        implements FragmentHomeContract.Presenter {

    @Override
    public void loadMonthRecipe(Context context) {
        Disposable disposable =
                Single.create(new SingleOnSubscribe<List<MonthRecipe>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<MonthRecipe>> emitter) throws Exception {
//                        SimpleModelImpl simpleModel = new SimpleModelImpl();
                        MonthRecipeModel monthRecipeModelImpl = new MonthRecipeModelImpl();
                        emitter.onSuccess(monthRecipeModelImpl.loadMonthRecipe(context));
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<MonthRecipe>>() {
                            @Override
                            public void accept(List<MonthRecipe> listMonthRecipe) throws Exception {
                                FragmentHomePresenter.this.getView().updateMonthRecipeMenu(listMonthRecipe);
                            }
                        });
        addDisposable(disposable);
    }

    @Override
    public void loadCategoryField(Context context) {
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
                                FragmentHomePresenter.this.getView().updateCategory(strCategorys);
                            }
                        });
        addDisposable(disposable);
    }
}
