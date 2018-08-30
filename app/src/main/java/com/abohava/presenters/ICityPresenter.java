package com.abohava.presenters;

import com.abohava.activities.ICityActivity;
import com.abohava.base.IPresenter;

public interface ICityPresenter<V extends ICityActivity> extends IPresenter<V> {

    void addCity(String cityName);

    void addCity(double latitude, double longitude);

    void deleteCityByName(String cityName);
}
