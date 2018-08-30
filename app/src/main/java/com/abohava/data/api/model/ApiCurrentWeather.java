package com.abohava.data.api.model;

import com.google.gson.annotations.SerializedName;

public class ApiCurrentWeather extends ApiBaseWeather {

    @SerializedName("city_name")
    private String mCityName;
    @SerializedName("lat")
    private float mLatitude;
    @SerializedName("lon")
    private float mLongitude;
    @SerializedName("app_temp")
    private float mApparentTemp;
    @SerializedName("wind_cdir_full")
    private String mWindDirection;
    @SerializedName("sunrise")
    private String mSunrise;
    @SerializedName("sunset")
    private String mSunset;

    public String getCityName() {
        return mCityName;
    }

    public float getLatitude() {
        return mLatitude;
    }

    public float getLongitude() {
        return mLongitude;
    }

    public float getApparentTemp() {
        return mApparentTemp;
    }

    public String getWindDirection() {
        return mWindDirection;
    }

    public String getSunrise() {
        return mSunrise;
    }

    public String getSunset() {
        return mSunset;
    }
}
