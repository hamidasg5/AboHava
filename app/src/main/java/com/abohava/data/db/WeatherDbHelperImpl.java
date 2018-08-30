package com.abohava.data.db;

import com.abohava.data.db.model.City;
import com.abohava.data.db.model.CityFields;
import com.abohava.data.db.model.CurrentWeather;
import com.abohava.data.db.model.ForecastWeather;
import com.abohava.data.db.model.HistoryWeather;
import com.abohava.dependencies.LooperScheduler;
import com.abohava.dependencies.WriteScheduler;
import com.abohava.data.db.DbErrors.*;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.Disposables;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

@Singleton
public class WeatherDbHelperImpl implements WeatherDbHelper {

    private final Scheduler mLooperScheduler;
    private final Scheduler mWriteScheduler;

    @Inject
    WeatherDbHelperImpl(@LooperScheduler Scheduler looperScheduler, @WriteScheduler Scheduler writeScheduler) {
        mLooperScheduler = looperScheduler;
        mWriteScheduler = writeScheduler;
        initDb();
    }

    private void initDb() {
        getCityList().flatMapCompletable(cities -> {
            if (cities.isEmpty()) {
                City tehran = new City();
                tehran.setName("Tehran");
                return insertCity(tehran);
            }
            return Completable.complete();
        }).subscribe();
    }

    @Override
    public Single<City> getCityById(String cityId) {
        return getLiveCityById(cityId).firstElement().toSingle();
    }

    @Override
    public Single<City> getCityByName(String cityName) {
        return getLiveCityByName(cityName).firstElement().toSingle();
    }

    @Override
    public Single<List<City>> getCityList() {
        return getLiveCityList().first(new ArrayList<>());
    }

    @Override
    public Flowable<City> getLiveCityById(String cityId) {
        return Flowable.create((FlowableOnSubscribe<City>) emitter -> {
            Realm realm = Realm.getDefaultInstance();
            final RealmResults<City> results = realm.where(City.class).equalTo(CityFields.ID, cityId).findAll();
            if (results.isEmpty()) {
                emitter.onError(new NoCityForIdException("No city found for the id: " + cityId));
            } else {
                results.addChangeListener(realmResults -> {
                    if (!realmResults.isEmpty()) {
                        List<City> cities = realm.copyFromRealm(realmResults);
                        if (!emitter.isCancelled()) {
                            emitter.onNext(cities.get(0));
                        }
                    }
                });
                emitter.setDisposable(Disposables.fromAction(() -> {
                    if (results.isValid()) {
                        results.removeAllChangeListeners();
                    }
                    if (!realm.isClosed()) {
                        realm.close();
                    }
                }));
                emitter.onNext(realm.copyFromRealm(results).get(0));
            }
        }, BackpressureStrategy.LATEST).subscribeOn(mLooperScheduler).unsubscribeOn(mLooperScheduler);
    }

    @Override
    public Flowable<City> getLiveCityByName(String cityName) {
        return Flowable.create((FlowableOnSubscribe<City>) emitter -> {
            Realm realm = Realm.getDefaultInstance();
            final RealmResults<City> results = realm.where(City.class).equalTo(CityFields.NAME, cityName).findAll();
            if (results.isEmpty()) {
                emitter.onError(new NoCityForNameException("No city found for the name: " + cityName));
            } else {
                results.addChangeListener(realmResults -> {
                    if (!realmResults.isEmpty()) {
                        List<City> cities = realm.copyFromRealm(realmResults);
                        if (!emitter.isCancelled()) {
                            emitter.onNext(cities.get(0));
                        }
                    }
                });
                emitter.setDisposable(Disposables.fromAction(() -> {
                    if (results.isValid()) {
                        results.removeAllChangeListeners();
                    }
                    if (!realm.isClosed()) {
                        realm.close();
                    }
                }));
                emitter.onNext(realm.copyFromRealm(results).get(0));
            }
        }, BackpressureStrategy.LATEST).subscribeOn(mLooperScheduler).unsubscribeOn(mLooperScheduler);
    }

    @Override
    public Flowable<List<City>> getLiveCityList() {
        return Flowable.create((FlowableOnSubscribe<List<City>>) emitter -> {
            Realm realm = Realm.getDefaultInstance();
            final RealmResults<City> results = realm.where(City.class).sort(CityFields.NAME, Sort.DESCENDING).findAll();
            results.addChangeListener(realmResults -> {
                List<City> cityList = realm.copyFromRealm(realmResults);
                if (!emitter.isCancelled()) {
                    emitter.onNext(cityList);
                }
            });
            emitter.setDisposable(Disposables.fromAction(() -> {
                if (results.isValid()) {
                    results.removeAllChangeListeners();
                }
                if (!realm.isClosed()) {
                    realm.close();
                }
            }));
            emitter.onNext(realm.copyFromRealm(results));
        }, BackpressureStrategy.LATEST).subscribeOn(mLooperScheduler).unsubscribeOn(mLooperScheduler);
    }

    @Override
    public Completable insertCity(City city) {
        return Completable.create(emitter -> {
            try (Realm realmInstance = Realm.getDefaultInstance()) {
                if (realmInstance.where(City.class).equalTo(CityFields.NAME, city.getName()).findFirst() == null) {
                    realmInstance.executeTransaction(realm -> realm.insert(city));
                    emitter.onComplete();
                } else {
                    emitter.onError(new CityHasDuplicateException(city.getName() + " is already added"));
                }
            }
        }).subscribeOn(mWriteScheduler);
    }

    @Override
    public Completable deleteCityByName(String cityName) {
        return getCityList().flatMapCompletable(cities -> Completable.create(emitter -> {
            if (cities.size() <= 1) {
                emitter.onError(new CityListEmptyException("City list cannot has zero elements"));
            } else {
                try (Realm realmInstance = Realm.getDefaultInstance()) {
                    realmInstance.executeTransaction(realm -> {
                        City city = realm.where(City.class).equalTo(CityFields.NAME, cityName).findFirst();
                        if (city == null) {
                            emitter.onError(new NoCityForNameException("No city found for name: " + cityName));
                        } else {
                            city.getHistoryHourlyWeathers().deleteAllFromRealm();
                            city.getForecastHourlyWeathers().deleteAllFromRealm();
                            city.getForecastDailyWeathers().deleteAllFromRealm();
                            if (city.getCurrentWeather() != null) {
                                city.getCurrentWeather().deleteFromRealm();
                            }
                            city.deleteFromRealm();
                        }
                    });
                    emitter.onComplete();
                }
            }
        })).subscribeOn(mWriteScheduler);
    }

    @Override
    public Completable persistCityHistory(String cityId, List<HistoryWeather> historyWeathers) {
        return Completable.create(emitter -> {
            if (historyWeathers.isEmpty()) {
                emitter.onError(new ApiConnectionException("fetching history weathers failed for " + cityId));
            } else {
                try (Realm realmInstance = Realm.getDefaultInstance()) {
                    realmInstance.executeTransaction(realm -> {
                        City city = realm.where(City.class).equalTo(CityFields.ID, cityId).findFirst();
                        if (city != null) {
                            List<HistoryWeather> managedWeathers = realm.copyToRealm(historyWeathers);
                            RealmList<HistoryWeather> managedList = new RealmList<>();
                            managedList.addAll(managedWeathers);
                            city.getHistoryHourlyWeathers().deleteAllFromRealm();
                            city.setHistoryHourlyWeathers(managedList);
                            city.setHistoryLastUpdate(System.currentTimeMillis());
                        }
                    });
                }
            }
        }).subscribeOn(mWriteScheduler);
    }

    @Override
    public Completable persistCityCurrent(String cityId, List<CurrentWeather> currentWeathers) {
        return Completable.create(emitter -> {
            if (currentWeathers.isEmpty()) {
                emitter.onError(new ApiConnectionException("fetching current weather failed for " + cityId));
            } else {
                try (Realm realmInstance = Realm.getDefaultInstance()) {
                    realmInstance.executeTransaction(realm -> {
                        City city = realm.where(City.class).equalTo(CityFields.ID, cityId).findFirst();
                        if (city != null) {
                            CurrentWeather currentWeather = currentWeathers.get(0);
                            City dbCity = realm.where(City.class).equalTo(CityFields.NAME, currentWeather.getCityName()).findFirst();
                            if (dbCity == null || dbCity.getId().equals(cityId)) {
                                CurrentWeather weather = city.getCurrentWeather();
                                if (weather != null) {
                                    weather.deleteFromRealm();
                                }
                                city.setCurrentWeather(realm.copyToRealm(currentWeather));
                                city.setCurrentLastUpdate(System.currentTimeMillis());
                                city.setName(currentWeather.getCityName());
                                city.setLatitude(currentWeather.getLatitude());
                                city.setLongitude(currentWeather.getLongitude());
                            } else {
                                city.deleteFromRealm();
                                emitter.onError(new CityHasDuplicateException("City is already added"));
                            }
                        }
                    });
                }
            }
        }).subscribeOn(mWriteScheduler);
    }

    @Override
    public Completable persistCityForecastHourly(String cityId, List<ForecastWeather> forecastWeathers) {
        return Completable.create(emitter -> {
            if (forecastWeathers.isEmpty()) {
                emitter.onError(new ApiConnectionException("fetching forecast weathers failed for " + cityId));
            } else {
                try (Realm realmInstance = Realm.getDefaultInstance()) {
                    realmInstance.executeTransaction(realm -> {
                        City city = realm.where(City.class).equalTo(CityFields.ID, cityId).findFirst();
                        if (city != null) {
                            List<ForecastWeather> managedWeathers = realm.copyToRealm(forecastWeathers);
                            RealmList<ForecastWeather> managedList = new RealmList<>();
                            managedList.addAll(managedWeathers);
                            city.getForecastHourlyWeathers().deleteAllFromRealm();
                            city.setForecastHourlyWeathers(managedList);
                            city.setForecastHLastUpdate(System.currentTimeMillis());
                        }
                    });
                }
            }
        }).subscribeOn(mWriteScheduler);
    }

    @Override
    public Completable persistCityForecastDaily(String cityId, List<ForecastWeather> forecastWeathers) {
        return Completable.create(emitter -> {
            if (forecastWeathers.isEmpty()) {
                emitter.onError(new ApiConnectionException("fetching forecast weathers failed for " + cityId));
            } else {
                try (Realm realmInstance = Realm.getDefaultInstance()) {
                    realmInstance.executeTransaction(realm -> {
                        City city = realm.where(City.class).equalTo(CityFields.ID, cityId).findFirst();
                        if (city != null) {
                            List<ForecastWeather> managedWeathers = realm.copyToRealm(forecastWeathers);
                            RealmList<ForecastWeather> managedList = new RealmList<>();
                            managedList.addAll(managedWeathers);
                            city.getForecastDailyWeathers().deleteAllFromRealm();
                            city.setForecastDailyWeathers(managedList);
                            city.setForecastDLastUpdate(System.currentTimeMillis());
                        }
                    });
                }
            }
        }).subscribeOn(mWriteScheduler);
    }
}
