package com.abohava.data.api.model;

import com.google.gson.annotations.SerializedName;

public class ApiHistoryWeather extends ApiBaseWeather {

    @SerializedName("datetime")
    private String mDateTime;

    public String getDateTime() {
        return mDateTime;
    }
}
