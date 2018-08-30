package com.abohava.presenters;

import android.util.Log;

import com.abohava.R;
import com.abohava.activities.ICityActivity;
import com.abohava.base.BasePresenter;
import com.abohava.data.DataManager;
import com.abohava.data.db.DbErrors;
import com.abohava.data.db.model.City;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class CityPresenter<V extends ICityActivity> extends BasePresenter<V> implements ICityPresenter<V> {

    private static final String TAG = "CityPresenter";

    @Inject
    CityPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);

        getCompositeDisposable().add(getDataManager()
                .getLiveCityList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        cities -> {
                            if (assertView()) {
                                getMvpView().showCityItems(cities);
                            }
                        }, throwable -> {
                            throwable.printStackTrace();
                            Log.e(TAG, "onAttach: ", throwable);
                        })
        );
    }

    @Override
    public void addCity(String cityName) {
        City city = new City();
        city.setName(cityName);
        getCompositeDisposable().add(getDataManager()
                .insertCity(city)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {},
                        throwable -> {
                            throwable.printStackTrace();
                            Log.e(TAG, "addCityByName: ", throwable);
                            if (throwable instanceof DbErrors.CityHasDuplicateException) {
                                showUpdateMessage(R.string.city_is_already_in_list);
                            }
                        })
        );
    }

    @Override
    public void addCity(double latitude, double longitude) {
        City city = new City();
        city.setLatitude((float) latitude);
        city.setLongitude((float) longitude);
        city.setName(latitude + ", " + longitude);
        getCompositeDisposable().add(getDataManager()
                .insertCity(city)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> getCompositeDisposable().add(getDataManager()
                                .updateCityCurrentWeather(city.getId())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        () -> showUpdateMessage(R.string.update_successful),
                                        throwable -> {
                                            throwable.printStackTrace();
                                            Log.e(TAG, "addCityByLatLonOnUpdate: ", throwable);
                                            if (throwable instanceof DbErrors.ApiConnectionException) {
                                                showUpdateMessage(R.string.loading_failure);
                                            }
                                            if (throwable instanceof DbErrors.CityHasDuplicateException) {
                                                showUpdateMessage(R.string.city_is_already_in_list);
                                            }
                                        })
                        ), throwable -> {
                            throwable.printStackTrace();
                            Log.e(TAG, "addCityLatLon: ", throwable);
                        })
        );
    }

    @Override
    public void deleteCityByName(String cityName) {
        getCompositeDisposable().add(getDataManager()
                .deleteCityByName(cityName)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {},
                        throwable -> {
                            throwable.printStackTrace();
                            Log.e(TAG, "deleteCityByName: ", throwable);
                            if (throwable instanceof DbErrors.CityListEmptyException) {
                                showUpdateMessage(R.string.list_cannot_has_zero_items);
                            }
                        })
        );
    }

    private void showUpdateMessage(int messageRes) {
        if (assertView()) {
            getMvpView().showToast(messageRes);
        }
    }
}
