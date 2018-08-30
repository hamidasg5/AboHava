package com.abohava.data.db;

import com.abohava.data.db.model.City;
import com.abohava.data.db.model.CurrentWeather;
import com.abohava.data.db.model.ForecastWeather;
import com.abohava.data.db.model.HistoryWeather;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface WeatherDbHelper {

    Single<City> getCityById(String cityId);

    Single<City> getCityByName(String cityName);

    Single<List<City>> getCityList();

    Flowable<City> getLiveCityById(String cityId);

    Flowable<City> getLiveCityByName(String cityName);

    Flowable<List<City>> getLiveCityList();

    Completable insertCity(City city);

    Completable deleteCityByName(String cityName);

    Completable persistCityHistory(String cityId, List<HistoryWeather> historyWeathers);

    Completable persistCityCurrent(String cityId, List<CurrentWeather> currentWeathers);

    Completable persistCityForecastHourly(String cityId, List<ForecastWeather> forecastWeathers);

    Completable persistCityForecastDaily(String cityId, List<ForecastWeather> forecastWeathers);
}
