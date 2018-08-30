package com.abohava.presenters;

import android.graphics.PointF;
import android.util.Log;

import com.abohava.R;
import com.abohava.activities.IHistoryActivity;
import com.abohava.base.BasePresenter;
import com.abohava.data.DataManager;
import com.abohava.data.db.DbErrors;
import com.abohava.data.db.model.City;
import com.abohava.data.db.model.HistoryWeather;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class HistoryPresenter<V extends IHistoryActivity> extends BasePresenter<V> implements IHistoryPresenter<V> {

    private static final String TAG = "HistoryPresenter";

    private String mCityId;
    private City mCity;

    @Inject
    HistoryPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
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
                            if (mCity.shouldUpdateHistory(getDataManager().isAutoUpdateOn())) {
                                fetchHistoryWeathers();
                            }
                        }, throwable -> {
                            throwable.printStackTrace();
                            Log.e(TAG, "onAttach: ", throwable);
                        }
                )
        );
    }

    private void fetchHistoryWeathers() {
        getCompositeDisposable().add(getDataManager()
                .updateCityHistoryWeather(mCityId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::updateView,
                        throwable -> {
                            throwable.printStackTrace();
                            Log.e(TAG, "fetchHistoryWeathers: ", throwable);
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
        List<HistoryWeather> historyWeathers = mCity.getHistoryHourlyWeathers();
        String units = getDataManager().getUnits();
        if (!historyWeathers.isEmpty()) {
            List<PointF> points = new ArrayList<>();
            for (int i = 0; i < historyWeathers.size(); i++) {
                HistoryWeather weather = historyWeathers.get(i);
                if (weather != null) {
                    points.add(new PointF(i, weather.getTemp()));
                }
            }
            if (assertView()) {
                getMvpView().showHistoryWeathers(historyWeathers, units);
                getMvpView().plotTemps(points, units);
            }
        }
    }
}
