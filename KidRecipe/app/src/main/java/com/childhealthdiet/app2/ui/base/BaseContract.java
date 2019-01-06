package com.childhealthdiet.app2.ui.base;

/**
 * Created by newbiechen on 17-4-26.
 */

public interface BaseContract {

    interface BasePresenter<T> {

        void attachView(T view);

        void detachView();

        boolean isViewAttached();

        T getView();
    }

    interface BaseView {

        void showError();

        void complete();
    }
}
