package com.abohava.activities;

import android.graphics.PointF;

import com.abohava.base.IView;
import com.abohava.data.db.model.HistoryWeather;

import java.util.List;

public interface IHistoryActivity extends IView {

    String getCityId();

    void plotTemps(List<PointF> points, String units);

    void showHistoryWeathers(List<HistoryWeather> historyWeathers, String units);
}
