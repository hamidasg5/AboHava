package com.abohava.dependencies.components;

import com.abohava.activities.CityActivity;
import com.abohava.activities.ForecastActivity;
import com.abohava.activities.HistoryActivity;
import com.abohava.activities.SearchActivity;
import com.abohava.activities.WeatherActivity;
import com.abohava.dependencies.PerActivity;
import com.abohava.dependencies.modules.ActivityModule;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(WeatherActivity weatherActivity);

    void inject(HistoryActivity historyActivity);

    void inject(ForecastActivity forecastActivity);

    void inject(CityActivity cityActivity);

    void inject(SearchActivity searchActivity);
}
