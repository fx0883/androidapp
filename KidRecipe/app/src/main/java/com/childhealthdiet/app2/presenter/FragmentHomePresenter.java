package com.childhealthdiet.app2.presenter;

import android.content.Context;

import com.childhealthdiet.app2.model.SimpleModelImpl;
import com.childhealthdiet.app2.presenter.contract.FragmentHomeContract;
import com.childhealthdiet.app2.ui.base.BaseMVPFragment;
import com.childhealthdiet.app2.ui.base.RxPresenter;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FragmentHomePresenter extends RxPresenter<FragmentHomeContract.View>
        implements FragmentHomeContract.Presenter {


//    Single<String[]> single = Single.create(SingleOnSubscribe<String[]>)
    void loadMonth(){
//        Disposable disposable = RemoteRepository
//                .getInstance()
//                .getHotComments(bookId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        (value) -> mView.finishHotComment(value)
//                );
//        addDisposable(disposable);
    }

    @Override
    public void loadMonthRecipe(Context context) {

    }

    @Override
    public void loadCategoryField(Context context) {
        Disposable disposable =


                Single.create(new SingleOnSubscribe<String[]>() {

            @Override
            public void subscribe(SingleEmitter<String[]> emitter) throws Exception {
                SimpleModelImpl simpleModel = new SimpleModelImpl();
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
