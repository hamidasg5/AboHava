package com.abohava.presenters;

import com.abohava.R;
import com.abohava.activities.ISearchActivity;
import com.abohava.base.BasePresenter;
import com.abohava.data.DataManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter<V extends ISearchActivity> extends BasePresenter<V> implements ISearchPresenter<V> {

    private List<String> mCityNames;

    @Inject
    SearchPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);
        getCompositeDisposable().add(getDataManager()
                .getCityNames()
                .subscribe(strings -> {
                    mCityNames = strings;
                    if (assertView()) {
                        getMvpView().showCityItems(mCityNames);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    mCityNames = new ArrayList<>();
                    if (assertView()) {
                        getMvpView().showSnackBar(R.string.loading_failure);
                    }
                }));
    }

    @Override
    public void searchCities(String query) {
        if (query.isEmpty()){
            if (assertView()) {
                getMvpView().showCityItems(mCityNames);
                return;
            }
        }
        getCompositeDisposable().add(Observable.fromIterable(mCityNames)
                .filter(s -> s.contains(query))
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(strings -> {
                    if (assertView()) {
                        getMvpView().showCityItems(strings);
                    }
                })
        );
    }
}
