package com.abohava.data.api;

import com.abohava.data.api.model.ApiCurrentWeather;
import com.abohava.data.api.model.ApiForecastWeather;
import com.abohava.data.api.model.ApiHistoryWeather;
import com.abohava.data.db.model.City;
import com.abohava.utils.DateUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

import static com.abohava.utils.Constants.API_KEY;

@Singleton
public class ApiHelperImpl implements ApiHelper {

    private WeatherClient weatherClient;

    @Inject
    ApiHelperImpl(WeatherClient weatherClient) {
        this.weatherClient = weatherClient;
    }

    @Override
    public Observable<List<ApiHistoryWeather>> fetchHistoricalHourlyWeather(City city) {
        String[] dates = DateUtils.getLast24Hours();
        if (city.getLatitude() != null && city.getLongitude() != null) {
            return weatherClient.fetchHistoricalHourlyWeather(
                    API_KEY,
                    String.valueOf(city.getLatitude()),
                    String.valueOf(city.getLongitude()),
                    dates[0],
                    dates[1]);
        } else {
            return weatherClient.fetchHistoricalHourlyWeather(
                    API_KEY,
                    city.getName(),
                    dates[0],
                    dates[1]);
        }
    }

    @Override
    public Observable<List<ApiCurrentWeather>> fetchCurrentWeather(City city) {
        if (city.getLatitude() != null && city.getLongitude() != null) {
            return weatherClient.fetchCurrentWeather(
                    API_KEY,
                    String.valueOf(city.getLatitude()),
                    String.valueOf(city.getLongitude()));
        } else {
            return weatherClient.fetchCurrentWeather(
                    API_KEY,
                    city.getName());
        }
    }

    @Override
    public Observable<List<ApiForecastWeather>> fetchForecastHourlyWeather(City city) {
        if (city.getLatitude() != null && city.getLongitude() != null) {
            return weatherClient.fetchForecastHourlyWeather(
                    API_KEY,
                    String.valueOf(city.getLatitude()),
                    String.valueOf(city.getLongitude()));
        } else {
            return weatherClient.fetchForecastHourlyWeather(
                    API_KEY,
                    city.getName());
        }
    }

    @Override
    public Observable<List<ApiForecastWeather>> fetchForecastDailyWeather(City city) {
        if (city.getLatitude() != null && city.getLongitude() != null) {
            return weatherClient.fetchForecastDailyWeather(
                    API_KEY,
                    String.valueOf(city.getLatitude()),
                    String.valueOf(city.getLongitude()));
        } else {
            return weatherClient.fetchForecastDailyWeather(
                    API_KEY,
                    city.getName());
        }
    }
}
