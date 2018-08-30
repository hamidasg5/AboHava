package com.abohava.presenters;

import android.graphics.PointF;
import android.util.Log;

import com.abohava.R;
import com.abohava.activities.IForecastActivity;
import com.abohava.base.BasePresenter;
import com.abohava.data.DataManager;
import com.abohava.data.db.DbErrors;
import com.abohava.data.db.model.City;
import com.abohava.data.db.model.ForecastWeather;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class ForecastPresenter<V extends IForecastActivity> extends BasePresenter<V> implements IForecastPresenter<V> {

    private static final String TAG = "ForecastPresenter";

    private String mCityId;
    private City mCity;

    @Inject
    ForecastPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onAttach(V mvpView) {
        super.onAttach(mvpView);

        mCityId = getMvpView().getCityId();

        getCompositeDisposable().add(getDataManager()
                .getLiveCityById(mCityId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        city -> {
                            mCity = city;
                            updateView();
                            if (mCity.shouldUpdateForecastHourly(getDataManager().isAutoUpdateOn())) {
                                fetchForecastWeathers();
                            }
                        }, throwable -> {
                            throwable.printStackTrace();
                            Log.e(TAG, "onAttach: ", throwable);
                        }
                )
        );
    }

    private void fetchForecastWeathers() {
        getCompositeDisposable().add(getDataManager()
                .updateCityForecastHourlyWeather(mCityId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::updateView,
                        throwable -> {
                            throwable.printStackTrace();
                            Log.e(TAG, "fetchForecastWeathers: ", throwable);
                            if (throwable instanceof DbErrors.ApiConnectionException) {
                                if (assertView()) {
                                    getMvpView().showToast(R.string.loading_failure);
                                }
                            }
                        }
                )
        );
    }

    private void updateView() {
        List<ForecastWeather> forecastWeathers = mCity.getForecastHourlyWeathers();
        String units = getDataManager().getUnits();
        if (!forecastWeathers.isEmpty()) {
            List<PointF> points = new ArrayList<>();
            for (int i = 0; i < forecastWeathers.size(); i++) {
                if (i > 23) {
                    break;
                }
                ForecastWeather weather = forecastWeathers.get(i);
                if (weather != null) {
                    points.add(new PointF(i, weather.getTemp()));
                }
            }
            if (assertView()) {
                getMvpView().showForecastWeathers(forecastWeathers, units);
                getMvpView().plotTemps(points, units);
            }
        }
    }
}
