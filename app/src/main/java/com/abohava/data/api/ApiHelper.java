package com.abohava.data.api;

import com.abohava.data.api.model.ApiCurrentWeather;
import com.abohava.data.api.model.ApiForecastWeather;
import com.abohava.data.api.model.ApiHistoryWeather;
import com.abohava.data.db.model.City;

import java.util.List;

import io.reactivex.Observable;

public interface ApiHelper {

    Observable<List<ApiHistoryWeather>> fetchHistoricalHourlyWeather(City city);

    Observable<List<ApiCurrentWeather>> fetchCurrentWeather(City city);

    Observable<List<ApiForecastWeather>> fetchForecastHourlyWeather(City city);

    Observable<List<ApiForecastWeather>> fetchForecastDailyWeather(City city);
}
