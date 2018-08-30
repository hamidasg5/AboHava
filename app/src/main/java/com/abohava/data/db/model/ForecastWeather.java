package com.abohava.data.db.model;

import io.realm.RealmObject;

public class ForecastWeather extends RealmObject {

    private Long mTimeStamp;

    private Integer mTemp;
    private String mWeatherIcon;
    private String mWeatherCode;

    private Integer mPrecipitationProbability;
    private Integer mRelativeHumidity;
    private Integer mCloudCoverage;

    private Integer mWindSpeed;
    private String mWindDirection;

    private Float mUV;

    public Long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        mTimeStamp = timeStamp;
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

    public Integer getPrecipitationProbability() {
        return mPrecipitationProbability;
    }

    public void setPrecipitationProbability(Integer precipitationProbability) {
        mPrecipitationProbability = precipitationProbability;
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

    public String getWindDirection() {
        return mWindDirection;
    }

    public void setWindDirection(String windDirection) {
        mWindDirection = windDirection;
    }

    public Float getUV() {
        return mUV;
    }

    public void setUV(Float UV) {
        mUV = UV;
    }
}
