package com.abohava.data.db.model;

import io.realm.RealmObject;

public class HistoryWeather extends RealmObject {

    private String mDateTime;

    private Integer mTemp;
    private String mWeatherIcon;
    private String mWeatherCode;

    private Integer mRelativeHumidity;
    private Integer mCloudCoverage;

    private Integer mWindSpeed;

    private Float mUV;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof HistoryWeather) {
            HistoryWeather historyWeather = (HistoryWeather) obj;
            return mDateTime.equals(historyWeather.mDateTime);
        }
        return false;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String dateTime) {
        mDateTime = dateTime;
    }

    public Integer getTemp() {
        return mTemp;
    }

    public void setTemp(Integer temp) {
        mTemp = temp;
    }

    public String getWeatherIcon() {
        return mWeatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        mWeatherIcon = weatherIcon;
    }

    public String getWeatherCode() {
        return mWeatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        mWeatherCode = weatherCode;
    }

    public Integer getRelativeHumidity() {
        return mRelativeHumidity;
    }

    public void setRelativeHumidity(Integer relativeHumidity) {
        mRelativeHumidity = relativeHumidity;
    }

    public Integer getCloudCoverage() {
        return mCloudCoverage;
    }

    public void setCloudCoverage(Integer cloudCoverage) {
        mCloudCoverage = cloudCoverage;
    }

    public Integer getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(Integer windSpeed) {
        mWindSpeed = windSpeed;
    }

    public Float getUV() {
        return mUV;
    }

    public void setUV(Float UV) {
        mUV = UV;
    }
}
