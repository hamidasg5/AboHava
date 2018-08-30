package com.abohava.data.db.model;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class City extends RealmObject {

    @PrimaryKey
    private String mId = UUID.randomUUID().toString();
    @Index
    private String mName;
    private Float mLatitude;
    private Float mLongitude;
    private Long mHistoryLastUpdate = 0L;
    private Long mCurrentLastUpdate = 0L;
    private Long mForecastHLastUpdate = 0L;
    private Long mForecastDLastUpdate = 0L;
    private RealmList<HistoryWeather> mHistoryHourlyWeathers;
    private CurrentWeather mCurrentWeather;
    private RealmList<ForecastWeather> mForecastHourlyWeathers;
    private RealmList<ForecastWeather> mForecastDailyWeathers;

    @Override
    public String toString() {
        return mName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof City) {
            City city = (City) obj;
            return city.mId.equals(mId) && city.mName.equals(mName);
        }
        return false;
    }

    public boolean shouldUpdateHistory(boolean autoUpdate) {
        return mHistoryLastUpdate == 0L || (System.currentTimeMillis() - mHistoryLastUpdate > 1800 * 1000 && autoUpdate);
    }

    public boolean shouldUpdateCurrent(boolean autoUpdate) {
        return mCurrentLastUpdate == 0L || (System.currentTimeMillis() - mCurrentLastUpdate > 1800 * 1000 && autoUpdate);
    }

    public boolean shouldUpdateForecastHourly(boolean autoUpdate) {
        return mForecastHLastUpdate == 0L || (System.currentTimeMillis() - mForecastHLastUpdate > 1800 * 1000 && autoUpdate);
    }

    public boolean shouldUpdateForecastDaily(boolean autoUpdate) {
        return mForecastDLastUpdate == 0L || (System.currentTimeMillis() - mForecastDLastUpdate > 1800 * 1000 && autoUpdate);
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Float getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Float latitude) {
        mLatitude = latitude;
    }

    public Float getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Float longitude) {
        mLongitude = longitude;
    }

    public Long getHistoryLastUpdate() {
        return mHistoryLastUpdate;
    }

    public void setHistoryLastUpdate(Long historyLastUpdate) {
        mHistoryLastUpdate = historyLastUpdate;
    }

    public Long getCurrentLastUpdate() {
        return mCurrentLastUpdate;
    }

    public void setCurrentLastUpdate(Long currentLastUpdate) {
        mCurrentLastUpdate = currentLastUpdate;
    }

    public Long getForecastHLastUpdate() {
        return mForecastHLastUpdate;
    }

    public void setForecastHLastUpdate(Long forecastHLastUpdate) {
        mForecastHLastUpdate = forecastHLastUpdate;
    }

    public Long getForecastDLastUpdate() {
        return mForecastDLastUpdate;
    }

    public void setForecastDLastUpdate(Long forecastDLastUpdate) {
        mForecastDLastUpdate = forecastDLastUpdate;
    }

    public RealmList<HistoryWeather> getHistoryHourlyWeathers() {
        return mHistoryHourlyWeathers;
    }

    public void setHistoryHourlyWeathers(RealmList<HistoryWeather> historyHourlyWeathers) {
        mHistoryHourlyWeathers = historyHourlyWeathers;
    }

    public CurrentWeather getCurrentWeather() {
        return mCurrentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        mCurrentWeather = currentWeather;
    }

    public RealmList<ForecastWeather> getForecastHourlyWeathers() {
        return mForecastHourlyWeathers;
    }

    public void setForecastHourlyWeathers(RealmList<ForecastWeather> forecastHourlyWeathers) {
        mForecastHourlyWeathers = forecastHourlyWeathers;
    }

    public RealmList<ForecastWeather> getForecastDailyWeathers() {
        return mForecastDailyWeathers;
    }

    public void setForecastDailyWeathers(RealmList<ForecastWeather> forecastDailyWeathers) {
        mForecastDailyWeathers = forecastDailyWeathers;
    }
}
