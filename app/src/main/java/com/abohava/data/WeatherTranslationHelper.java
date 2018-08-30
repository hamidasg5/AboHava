package com.abohava.data;

import com.abohava.data.api.model.ApiCurrentWeather;
import com.abohava.data.api.model.ApiForecastWeather;
import com.abohava.data.api.model.ApiHistoryWeather;
import com.abohava.data.db.model.CurrentWeather;
import com.abohava.data.db.model.ForecastWeather;
import com.abohava.data.db.model.HistoryWeather;
import com.abohava.utils.DateUtils;
import com.abohava.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class WeatherTranslationHelper {

    public static List<HistoryWeather> toHistoryWeather(List<ApiHistoryWeather> apiHistoryWeathers) {
        List<HistoryWeather> historyWeathers = new ArrayList<>(apiHistoryWeathers.size());
        for (ApiHistoryWeather apiWeather : apiHistoryWeathers) {
            HistoryWeather weather = new HistoryWeather();

            weather.setDateTime(DateUtils.toLocalTime(apiWeather.getDateTime().substring(11) + ":30"));
            weather.setTemp((int) apiWeather.getTemp());
            weather.setWeatherIcon(apiWeather.getWeatherDescription().getIcon());
            weather.setWeatherCode(apiWeather.getWeatherDescription().getCode());
            weather.setRelativeHumidity((int) apiWeather.getRelativeHumidity());
            weather.setCloudCoverage((int) apiWeather.getCloudCoverage());
            weather.setWindSpeed((int) apiWeather.getWindSpeed());
            weather.setUV(apiWeather.getUV());

            historyWeathers.add(weather);
        }
        return historyWeathers;
    }

    public static List<CurrentWeather> toCurrentWeather(List<ApiCurrentWeather> apiCurrentWeathers) {
        List<CurrentWeather> currentWeathers = new ArrayList<>(apiCurrentWeathers.size());
        for (ApiCurrentWeather apiWeather : apiCurrentWeathers) {
            CurrentWeather weather = new CurrentWeather();

            weather.setCityName(apiWeather.getCityName());
            weather.setLatitude(apiWeather.getLatitude());
            weather.setLongitude(apiWeather.getLongitude());
            weather.setTemp((int) apiWeather.getTemp());
            weather.setApparentTemp((int) apiWeather.getApparentTemp());
            weather.setWeatherIcon(apiWeather.getWeatherDescription().getIcon());
            weather.setWeatherCode(apiWeather.getWeatherDescription().getCode());
            weather.setRelativeHumidity((int) apiWeather.getRelativeHumidity());
            weather.setCloudCoverage((int) apiWeather.getCloudCoverage());
            weather.setWindSpeed((int) apiWeather.getWindSpeed());
            weather.setWindDirection(StringUtils.translateWindDir(apiWeather.getWindDirection()));
            weather.setSunrise(DateUtils.toLocalTime(apiWeather.getSunrise()));
            weather.setSunset(DateUtils.toLocalTime(apiWeather.getSunset()));
            weather.setUV(apiWeather.getUV());

            currentWeathers.add(weather);
        }
        return currentWeathers;
    }

    public static List<ForecastWeather> toForecastWeather(List<ApiForecastWeather> apiForecastWeathers) {
        List<ForecastWeather> forecastWeathers = new ArrayList<>(apiForecastWeathers.size());
        for (ApiForecastWeather apiWeather : apiForecastWeathers) {
            ForecastWeather weather = new ForecastWeather();

            weather.setTimeStamp(DateUtils.toLocalTimeStamp(apiWeather.getTimeStamp()));
            weather.setTemp((int) apiWeather.getTemp());
            weather.setWeatherIcon(apiWeather.getWeatherDescription().getIcon());
            weather.setWeatherCode(apiWeather.getWeatherDescription().getCode());
            weather.setRelativeHumidity((int) apiWeather.getRelativeHumidity());
            weather.setCloudCoverage((int) apiWeather.getCloudCoverage());
            weather.setPrecipitationProbability((int) apiWeather.getPrecipitationProbability());
            weather.setWindSpeed((int) apiWeather.getWindSpeed());
            weather.setWindDirection(StringUtils.translateWindDir(apiWeather.getWindDirection()));
            weather.setUV(apiWeather.getUV());

            forecastWeathers.add(weather);
        }
        return forecastWeathers;
    }
}
