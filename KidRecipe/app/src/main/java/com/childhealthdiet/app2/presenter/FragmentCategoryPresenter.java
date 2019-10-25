package com.ChildHealthDiet.app2.presenter;

import android.content.Context;
import com.ChildHealthDiet.app2.model.SimpleModelImpl;
import com.ChildHealthDiet.app2.model.bean.RecipeCategory;
import com.ChildHealthDiet.app2.model.contract.SimpleModel;
import com.ChildHealthDiet.app2.presenter.contract.CategorysContract;
import com.ChildHealthDiet.app2.ui.base.RxPresenter;
import java.util.List;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FragmentCategoryPresenter extends RxPresenter<CategorysContract.View>
        implements CategorysContract.Presenter {

    @Override
    public void getCategorysList(Context context) {

            Disposable disposable =
            Single.create(new SingleOnSubscribe<List<RecipeCategory>>() {
                @Override
                public void subscribe(SingleEmitter<List<RecipeCategory>> emitter) throws Exception {
                    SimpleModel simpleModel = new SimpleModelImpl();
                    emitter.onSuccess(simpleModel.loadCategoryslist(context));
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<RecipeCategory>>() {
                        @Override
                        public void accept(List<RecipeCategory>  allcategoryslist) throws Exception {
                            FragmentCategoryPresenter.this.getView().updateCategorysList(allcategoryslist);
                        }
                    });
    addDisposable(disposable);

    }
}
