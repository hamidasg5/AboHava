package com.abohava.activities;

import android.graphics.PointF;

import com.abohava.base.IView;
import com.abohava.data.db.model.City;

import java.util.List;

public interface IWeatherActivity extends IView {

    void showCityCount(int count);
    void showCurrentWeather(int index, City city, String units);
    void showDailyWeathers(int index, City city, String units);
    void plotTemps(List<PointF> points, String units);
}
