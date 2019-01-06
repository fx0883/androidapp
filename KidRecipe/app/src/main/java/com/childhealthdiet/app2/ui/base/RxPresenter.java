package com.childhealthdiet.app2.ui.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by newbiechen on 17-4-26.
 */

public class RxPresenter<T extends BaseContract.BaseView> implements BaseContract.BasePresenter<T> {

    protected Reference<T> mViewRef;
    protected CompositeDisposable mDisposable;

    protected void unSubscribe() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    protected void addDisposable(Disposable subscription) {
        if (mDisposable == null) {
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(subscription);
    }

    @Override
    public void attachView(T view) {

        this.mViewRef = new WeakReference<T>(view);
    }


    @Override
    public void detachView() {
        if(mViewRef != null){
            mViewRef.clear();
        }
        this.mViewRef = null;
        unSubscribe();
    }

    @Override
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    @Override
    public T getView() {
        return mViewRef.get();
    }
}
