package com.abohava.data;

import android.util.Log;

import com.abohava.data.api.ApiHelper;
import com.abohava.data.db.WeatherDbHelper;
import com.abohava.data.db.model.City;
import com.abohava.data.db.model.CurrentWeather;
import com.abohava.data.db.model.ForecastWeather;
import com.abohava.data.db.model.HistoryWeather;
import com.abohava.data.files.FileHelper;
import com.abohava.data.prefs.PrefsHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;

@Singleton
public class DataManagerImpl implements DataManager {

    private static final String TAG = "DataManagerImpl";

    private ApiHelper apiHelper;
    private WeatherDbHelper weatherDbHelper;
    private FileHelper fileHelper;
    private PrefsHelper prefsHelper;

    @Inject
    DataManagerImpl(
            ApiHelper apiHelper,
            WeatherDbHelper weatherDbHelper,
            PrefsHelper prefsHelper,
            FileHelper fileHelper) {

        this.apiHelper = apiHelper;
        this.weatherDbHelper = weatherDbHelper;
        this.prefsHelper = prefsHelper;
        this.fileHelper = fileHelper;
        Log.d(TAG, "DataManagerImpl: ");
    }

    @Override
    public Completable updateCityHistoryWeather(String cityId) {
        return getCityById(cityId)
                .flatMap((Function<City, SingleSource<List<HistoryWeather>>>) city -> apiHelper.fetchHistoricalHourlyWeather(city)
                        .first(new ArrayList<>())
                        .map(WeatherTranslationHelper::toHistoryWeather))
                .flatMapCompletable(historyWeathers -> weatherDbHelper.persistCityHistory(cityId, historyWeathers));
    }

    @Override
    public Completable updateCityCurrentWeather(String cityId) {
        return getCityById(cityId)
                .flatMap((Function<City, SingleSource<List<CurrentWeather>>>) city -> apiHelper.fetchCurrentWeather(city)
                        .first(new ArrayList<>())
                        .map(WeatherTranslationHelper::toCurrentWeather))
                .flatMapCompletable(currentWeathers -> weatherDbHelper.persistCityCurrent(cityId, currentWeathers));
    }

    @Override
    public Completable updateCityForecastHourlyWeather(String cityId) {
        return getCityById(cityId)
                .flatMap((Function<City, SingleSource<List<ForecastWeather>>>) city -> apiHelper.fetchForecastHourlyWeather(city)
                        .first(new ArrayList<>())
                        .map(WeatherTranslationHelper::toForecastWeather))
                .flatMapCompletable(forecastWeathers -> weatherDbHelper.persistCityForecastHourly(cityId, forecastWeathers));
    }

    @Override
    public Completable updateCityForecastDailyWeather(String cityId) {
        return getCityById(cityId)
                .flatMap((Function<City, SingleSource<List<ForecastWeather>>>) city -> apiHelper.fetchForecastDailyWeather(city)
                        .first(new ArrayList<>())
                        .map(WeatherTranslationHelper::toForecastWeather))
                .flatMapCompletable(forecastWeathers -> weatherDbHelper.persistCityForecastDaily(cityId, forecastWeathers));
    }

    @Override
    public Single<City> getCityById(String cityId) {
        return weatherDbHelper.getCityById(cityId);
    }

    @Override
    public Single<City> getCityByName(String cityName) {
        return weatherDbHelper.getCityByName(cityName);
    }

    @Override
    public Single<List<City>> getCityList() {
        return weatherDbHelper.getCityList();
    }

    @Override
    public Flowable<City> getLiveCityById(String cityId) {
        return weatherDbHelper.getLiveCityById(cityId);
    }

    @Override
    public Flowable<City> getLiveCityByName(String cityName) {
        return weatherDbHelper.getLiveCityByName(cityName);
    }

    @Override
    public Flowable<List<City>> getLiveCityList() {
        return weatherDbHelper.getLiveCityList();
    }

    @Override
    public Completable insertCity(City city) {
        return weatherDbHelper.insertCity(city);
    }

    @Override
    public Completable deleteCityByName(String cityName) {
        return weatherDbHelper.deleteCityByName(cityName);
    }

    @Override
    public Single<List<String>> getCityNames() {
        return fileHelper.getCityNames();
    }

    @Override
    public boolean isAutoUpdateOn() {
        return prefsHelper.isAutoUpdateOn();
    }

    @Override
    public String getUnits() {
        return prefsHelper.getUnits();
    }

    @Override
    public int getPageIndex() {
        return prefsHelper.getPageIndex();
    }

    @Override
    public void setPageIndex(int index) {
        prefsHelper.setPageIndex(index);
    }
}
