package com.abohava.data.api.model;

import com.google.gson.annotations.SerializedName;

public class WeatherDescription {

    @SerializedName("icon")
    private String mIcon;
    @SerializedName("code")
    private String mCode;
    @SerializedName("description")
    private String mDescription;

    public String getIcon() {
        return mIcon;
    }

    public String getCode() {
        return mCode;
    }

    public String getDescription() {
        return mDescription;
    }
}
