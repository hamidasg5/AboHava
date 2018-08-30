package com.abohava.data.api;

import com.abohava.data.api.model.ApiCurrentWeather;
import com.abohava.data.api.model.ApiForecastWeather;
import com.abohava.data.api.model.ApiHistoryWeather;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherClient {

    @GET("history/hourly?units=M")
    Observable<List<ApiHistoryWeather>> fetchHistoricalHourlyWeather(
            @Query("key") String apiKey,
            @Query("city") String city,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate
    );

    @GET("history/hourly?units=M")
    Observable<List<ApiHistoryWeather>> fetchHistoricalHourlyWeather(
            @Query("key") String apiKey,
            @Query("lat") String latitude,
            @Query("lon") String longitude,
            @Query("start_date") String startDate,
            @Query("end_date") String endDate
    );

    @GET("current?units=M")
    Observable<List<ApiCurrentWeather>> fetchCurrentWeather(
            @Query("key") String apiKey,
            @Query("city") String city
    );

    @GET("current?units=M")
    Observable<List<ApiCurrentWeather>> fetchCurrentWeather(
            @Query("key") String apiKey,
            @Query("lat") String latitude,
            @Query("lon") String longitude
    );

    @GET("forecast/hourly?units=M")
    Observable<List<ApiForecastWeather>> fetchForecastHourlyWeather(
            @Query("key") String apiKey,
            @Query("city") String city
    );

    @GET("forecast/hourly?units=M")
    Observable<List<ApiForecastWeather>> fetchForecastHourlyWeather(
            @Query("key") String apiKey,
            @Query("lat") String latitude,
            @Query("lon") String longitude
    );

    @GET("forecast/daily?units=M")
    Observable<List<ApiForecastWeather>> fetchForecastDailyWeather(
            @Query("key") String apiKey,
            @Query("city") String city
    );

    @GET("forecast/daily?units=M")
    Observable<List<ApiForecastWeather>> fetchForecastDailyWeather(
            @Query("key") String apiKey,
            @Query("lat") String latitude,
            @Query("lon") String longitude
    );
}
