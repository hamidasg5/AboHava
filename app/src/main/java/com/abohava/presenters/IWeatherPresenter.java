package com.abohava.presenters;

import com.abohava.activities.IWeatherActivity;
import com.abohava.base.IPresenter;

public interface IWeatherPresenter<V extends IWeatherActivity> extends IPresenter<V> {

    void onSwipeRight();

    void onSwipeLeft();

    void onRefresh();

    int getPageIndex();

    String getCityId();

    String getCityName();

    void setPage(String cityID);
}
