package com.abohava.presenters;

import android.graphics.PointF;
import android.util.Log;

import com.abohava.activities.IWeatherActivity;
import com.abohava.base.BasePresenter;
import com.abohava.data.DataManager;
import com.abohava.data.db.model.City;
import com.abohava.data.db.model.ForecastWeather;
import com.abohava.data.db.model.HistoryWeather;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class WeatherPresenter<V extends IWeatherActivity> extends BasePresenter<V> implements IWeatherPresenter<V> {

    private static final String TAG = "WeatherPresenter";

    private List<City> mCityList;

    @Inject
    WeatherPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);

        getCompositeDisposable().add(getDataManager()
                .getLiveCityList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::updateWeathers,
                        throwable -> {
                            throwable.printStackTrace();
                            Log.e(TAG, "onAttach: ", throwable);
                        })
        );
    }

    @Override
    public void onSwipeRight() {
        int index = getDataManager().getPageIndex();
        if (index > 0) {
            index--;
            getDataManager().setPageIndex(index);
        }
        updateView();
    }

    @Override
    public void onSwipeLeft() {
        int index = getDataManager().getPageIndex();
        if (mCityList != null && !mCityList.isEmpty() && index < mCityList.size() - 1) {
            index++;
            getDataManager().setPageIndex(index);
        }
        updateView();
    }

    @Override
    public void onRefresh() {
        int index = getDataManager().getPageIndex();
        getCompositeDisposable().add(getDataManager()
                .updateCityHistoryWeather(mCityList.get(index).getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::updateView,
                        throwable -> {
                            throwable.printStackTrace();
                            Log.e(TAG, "onRefresh: ", throwable);
                        }
                )
        );
        getCompositeDisposable().add(getDataManager()
                .updateCityCurrentWeather(mCityList.get(index).getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::updateView,
                        throwable -> {
                            throwable.printStackTrace();
                            Log.e(TAG, "onRefresh: ", throwable);
                        }
                )
        );
        getCompositeDisposable().add(getDataManager()
                .updateCityForecastHourlyWeather(mCityList.get(index).getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::updateView,
                        throwable -> {
                            throwable.printStackTrace();
                            Log.e(TAG, "onRefresh: ", throwable);
                        }
                )
        );
        getCompositeDisposable().add(getDataManager()
                .updateCityForecastDailyWeather(mCityList.get(index).getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::updateView,
                        throwable -> {
                            throwable.printStackTrace();
                            Log.e(TAG, "onRefresh: ", throwable);
                        }
                )
        );
    }

    @Override
    public int getPageIndex() {
        return getDataManager().getPageIndex();
    }

    @Override
    public String getCityId() {
        return mCityList.get(getPageIndex()).getId();
    }

    @Override
    public String getCityName() {
        return mCityList.get(getPageIndex()).getName();
    }

    @Override
    public void setPage(String cityID) {
        for (int i = 0; i < mCityList.size(); i++) {
            if (cityID.equals(mCityList.get(i).getId())){
                getDataManager().setPageIndex(i);
                updateView();
                break;
            }
        }
    }

    private void updateWeathers(List<City> cityList) {
        mCityList = cityList;
        updateView();
        for (City city : mCityList) {
            if (city.shouldUpdateHistory(getDataManager().isAutoUpdateOn())) {
                getCompositeDisposable().add(getDataManager()
                        .updateCityHistoryWeather(city.getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::updateView,
                                throwable -> {
                                    throwable.printStackTrace();
                                    Log.e(TAG, "updateWeathers: ", throwable);
                                }
                        )
                );
            }
            if (city.shouldUpdateCurrent(getDataManager().isAutoUpdateOn())) {
                getCompositeDisposable().add(getDataManager()
                        .updateCityCurrentWeather(city.getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::updateView,
                                throwable -> {
                                    throwable.printStackTrace();
                                    Log.e(TAG, "updateWeathers: ", throwable);
                                }
                        )
                );
            }
            if (city.shouldUpdateForecastHourly(getDataManager().isAutoUpdateOn())) {
                getCompositeDisposable().add(getDataManager()
                        .updateCityForecastHourlyWeather(city.getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::updateView,
                                throwable -> {
                                    throwable.printStackTrace();
                                    Log.e(TAG, "updateWeathers: ", throwable);
                                }
                        )
                );
            }
            if (city.shouldUpdateForecastDaily(getDataManager().isAutoUpdateOn())) {
                getCompositeDisposable().add(getDataManager()
                        .updateCityForecastDailyWeather(city.getId())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                this::updateView,
                                throwable -> {
                                    throwable.printStackTrace();
                                    Log.e(TAG, "updateWeathers: ", throwable);
                                }
                        )
                );
            }
        }
    }

    private void updateView() {
        if (assertView() && !mCityList.isEmpty()) {

            getMvpView().showCityCount(mCityList.size());

            int index = getDataManager().getPageIndex();
            if (index < 0) {
                index = 0;
                getDataManager().setPageIndex(index);
            } else if (index >= mCityList.size()) {
                index = mCityList.size() - 1;
                getDataManager().setPageIndex(index);
            }
            City city = mCityList.get(index);
            String units = getDataManager().getUnits();
            if (city.getCurrentWeather() != null) {
                getMvpView().showCurrentWeather(index, city, units);
            }
            if (city.getForecastDailyWeathers().size() > 2) {
                getMvpView().showDailyWeathers(index, city, units);
            }
            List<ForecastWeather> forecastWeathers = city.getForecastHourlyWeathers();
            List<HistoryWeather> historyWeathers = city.getHistoryHourlyWeathers();
            if (forecastWeathers.size() > 11 && historyWeathers.size() == 24) {
                List<PointF> points = new ArrayList<>(24);
                for (int i = 12; i < 24; i++) {
                    HistoryWeather weather = historyWeathers.get(i);
                    if (weather != null) {
                        points.add(new PointF(i - 12, weather.getTemp()));
                    }
                }
                for (int i = 0; i < 12; i++) {
                    ForecastWeather weather = forecastWeathers.get(i);
                    if (weather != null) {
                        points.add(new PointF(i + 12, weather.getTemp()));
                    }
                }
                getMvpView().plotTemps(points, units);
            }
        }
    }
}
