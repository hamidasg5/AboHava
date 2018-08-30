package com.abohava.data;

import com.abohava.data.db.model.City;
import com.abohava.data.files.FileHelper;
import com.abohava.data.prefs.PrefsHelper;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface DataManager extends FileHelper, PrefsHelper {

    Single<City> getCityById(String cityId);

    Single<City> getCityByName(String cityName);

    Single<List<City>> getCityList();

    Flowable<City> getLiveCityById(String cityId);

    Flowable<City> getLiveCityByName(String cityName);

    Flowable<List<City>> getLiveCityList();

    Completable insertCity(City city);

    Completable deleteCityByName(String cityName);

    Completable updateCityHistoryWeather(String cityId);

    Completable updateCityCurrentWeather(String cityId);

    Completable updateCityForecastHourlyWeather(String cityId);

    Completable updateCityForecastDailyWeather(String cityId);
}
