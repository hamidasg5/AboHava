package com.abohava.data.db.model;

import io.realm.RealmObject;

public class CurrentWeather extends RealmObject {

    private String mCityName;
    private Float mLatitude;
    private Float mLongitude;

    private String mWeatherIcon;
    private Integer mTemp;
    private String mWeatherCode;
    private Integer mApparentTemp;

    private Integer mRelativeHumidity;
    private Integer mCloudCoverage;

    private Integer mWindSpeed;
    private String mWindDirection;

    private String mSunrise;
    private String mSunset;
    private Float mUV;

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
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

    public String getWeatherIcon() {
        return mWeatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        mWeatherIcon = weatherIcon;
    }

    public Integer getTemp() {
        return mTemp;
    }

    public void setTemp(Integer temp) {
        mTemp = temp;
    }

    public String getWeatherCode() {
        return mWeatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        mWeatherCode = weatherCode;
    }

    public Integer getApparentTemp() {
        return mApparentTemp;
    }

    public void setApparentTemp(Integer apparentTemp) {
        mApparentTemp = apparentTemp;
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

    public String getSunrise() {
        return mSunrise;
    }

    public void setSunrise(String sunrise) {
        mSunrise = sunrise;
    }

    public String getSunset() {
        return mSunset;
    }

    public void setSunset(String sunset) {
        mSunset = sunset;
    }

    public Float getUV() {
        return mUV;
    }

    public void setUV(Float UV) {
        mUV = UV;
    }
}
