package com.childhealthdiet.app2.presenter;

import android.content.Context;

import com.childhealthdiet.app2.model.SimpleModelImpl;
import com.childhealthdiet.app2.model.bean.MineItem;
import com.childhealthdiet.app2.model.bean.RecipeCategory;
import com.childhealthdiet.app2.model.contract.SimpleModel;
import com.childhealthdiet.app2.presenter.contract.CategorysContract;
import com.childhealthdiet.app2.presenter.contract.MineContract;
import com.childhealthdiet.app2.ui.base.RxPresenter;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MinePresenter extends RxPresenter<MineContract.View>
        implements MineContract.Presenter {



//    public void getCategorysList(Context context) {
//
//            Disposable disposable =
//            Single.create(new SingleOnSubscribe<List<RecipeCategory>>() {
//                @Override
//                public void subscribe(SingleEmitter<List<RecipeCategory>> emitter) throws Exception {
//                    SimpleModel simpleModel = new SimpleModelImpl();
//                    emitter.onSuccess(simpleModel.loadCategoryslist(context));
//                }
//            }).subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<List<RecipeCategory>>() {
//                        @Override
//                        public void accept(List<RecipeCategory>  allcategoryslist) throws Exception {
//                            MinePresenter.this.getView().updateCategorysList(allcategoryslist);
//                        }
//                    });
//    addDisposable(disposable);
//
//    }

    @Override
    public void getMineItems(Context context) {
        Disposable disposable =
                Single.create(new SingleOnSubscribe<List<MineItem>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<MineItem>> emitter) throws Exception {
                        SimpleModel simpleModel = new SimpleModelImpl();
                        emitter.onSuccess(simpleModel.loadMineitem(context));
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<MineItem>>() {
                            @Override
                            public void accept(List<MineItem>  mineItems) throws Exception {
                                MinePresenter.this.getView().updateMineItem(mineItems);
                            }
                        });
        addDisposable(disposable);
    }
}
