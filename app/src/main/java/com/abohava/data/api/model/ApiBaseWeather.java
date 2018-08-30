package com.abohava.data.api.model;

import com.google.gson.annotations.SerializedName;

public abstract class ApiBaseWeather {

    @SerializedName("temp")
    private float mTemp;
    @SerializedName("weather")
    private WeatherDescription mWeatherDescription;
    @SerializedName("rh")
    private float mRelativeHumidity;
    @SerializedName("clouds")
    private float mCloudCoverage;
    @SerializedName("wind_spd")
    private float mWindSpeed;
    @SerializedName("uv")
    private float mUV;

    public float getTemp() {
        return mTemp;
    }

    public WeatherDescription getWeatherDescription() {
        return mWeatherDescription;
    }

    public float getRelativeHumidity() {
        return mRelativeHumidity;
    }

    public float getCloudCoverage() {
        return mCloudCoverage;
    }

    public float getWindSpeed() {
        return mWindSpeed;
    }

    public float getUV() {
        return mUV;
    }
}
