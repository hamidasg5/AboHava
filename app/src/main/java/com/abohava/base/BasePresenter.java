package com.abohava.base;

import com.abohava.data.DataManager;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BasePresenter<V extends IView> implements IPresenter<V> {

    private final DataManager mDataManager;
    private final CompositeDisposable mCompositeDisposable;
    private V mMvpView;

    public BasePresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        mDataManager = dataManager;
        mCompositeDisposable = compositeDisposable;
    }

    protected DataManager getDataManager() {
        return mDataManager;
    }

    protected CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    protected V getMvpView() {
        return mMvpView;
    }

    protected boolean assertView() {
        return mMvpView != null;
    }

    @Override
    public void onAttach(V mvpView) {
        mMvpView = mvpView;
    }

    @Override
    public void onDetach() {
        mCompositeDisposable.dispose();
        mMvpView = null;
    }
}
