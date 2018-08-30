package com.abohava.activities;

import android.graphics.PointF;

import com.abohava.base.IView;
import com.abohava.data.db.model.ForecastWeather;

import java.util.List;

public interface IForecastActivity extends IView {

    String getCityId();

    void showForecastWeathers(List<ForecastWeather> forecastWeathers, String units);

    void plotTemps(List<PointF> points, String units);
}
