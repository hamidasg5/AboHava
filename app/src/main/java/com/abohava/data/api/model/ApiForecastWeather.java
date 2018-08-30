package com.abohava.data.api.model;

import com.google.gson.annotations.SerializedName;

public class ApiForecastWeather extends ApiBaseWeather {

    @SerializedName("ts")
    private Long mTimeStamp;
    @SerializedName("wind_cdir_full")
    private String mWindDirection;
    @SerializedName("pop")
    private float mPrecipitationProbability;

    public Long getTimeStamp() {
        return mTimeStamp;
    }

    public String getWindDirection() {
        return mWindDirection;
    }

    public float getPrecipitationProbability() {
        return mPrecipitationProbability;
    }
}
