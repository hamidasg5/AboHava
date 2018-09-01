package com.abohava.dependencies.modules;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.abohava.activities.ICityActivity;
import com.abohava.activities.IForecastActivity;
import com.abohava.activities.IHistoryActivity;
import com.abohava.activities.ISearchActivity;
import com.abohava.activities.IWeatherActivity;
import com.abohava.adapters.CityAdapter;
import com.abohava.adapters.ForecastAdapter;
import com.abohava.adapters.HistoryAdapter;
import com.abohava.adapters.SearchAdapter;
import com.abohava.dependencies.ActivityContext;
import com.abohava.dependencies.PerActivity;
import com.abohava.presenters.CityPresenter;
import com.abohava.presenters.ForecastPresenter;
import com.abohava.presenters.HistoryPresenter;
import com.abohava.presenters.ICityPresenter;
import com.abohava.presenters.IForecastPresenter;
import com.abohava.presenters.IHistoryPresenter;
import com.abohava.presenters.ISearchPresenter;
import com.abohava.presenters.IWeatherPresenter;
import com.abohava.presenters.SearchPresenter;
import com.abohava.presenters.WeatherPresenter;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityContext
    public Context provideContext() {
        return mActivity;
    }

    @Provides
    public AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    public CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    public FragmentManager provideFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }

    @Provides
    public LinearLayoutManager provideLinearLayoutManager(@ActivityContext Context context) {
        return new LinearLayoutManager(context);
    }

    @Provides
    public CityAdapter provideCityAdapter(@ActivityContext Context context, CompositeDisposable compositeDisposable) {
        return new CityAdapter(context, compositeDisposable);
    }

    @Provides
    public HistoryAdapter provideHistoryWeatherAdapter(@ActivityContext Context context, CompositeDisposable compositeDisposable) {
        return new HistoryAdapter(context, compositeDisposable);
    }

    @Provides
    public ForecastAdapter provideForecastAdapter(@ActivityContext Context context, CompositeDisposable compositeDisposable) {
        return new ForecastAdapter(context, compositeDisposable);
    }

    @Provides
    public SearchAdapter provideSearchAdapter(@ActivityContext Context context, CompositeDisposable compositeDisposable) {
        return new SearchAdapter(context, compositeDisposable);
    }

    @Provides
    @PerActivity
    public IWeatherPresenter<IWeatherActivity> provideWeatherPresenter(WeatherPresenter<IWeatherActivity> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    public IHistoryPresenter<IHistoryActivity> provideHistoryWeatherPresenter(HistoryPresenter<IHistoryActivity> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    public IForecastPresenter<IForecastActivity> provideForecastWeatherPresenter(ForecastPresenter<IForecastActivity> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    public ICityPresenter<ICityActivity> provideCityPresenter(CityPresenter<ICityActivity> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    public ISearchPresenter<ISearchActivity> provideSearchPresenter(SearchPresenter<ISearchActivity> presenter) {
        return presenter;
    }
}
