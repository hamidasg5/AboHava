package com.abohava.dependencies.modules;

import android.app.Application;
import android.content.Context;
import android.os.HandlerThread;

import com.abohava.data.DataManager;
import com.abohava.data.DataManagerImpl;
import com.abohava.data.api.ApiHelper;
import com.abohava.data.api.ApiHelperImpl;
import com.abohava.data.api.WeatherClient;
import com.abohava.data.api.WeatherDeserializer;
import com.abohava.data.api.model.ApiCurrentWeather;
import com.abohava.data.api.model.ApiForecastWeather;
import com.abohava.data.api.model.ApiHistoryWeather;
import com.abohava.data.db.WeatherDbHelper;
import com.abohava.data.db.WeatherDbHelperImpl;
import com.abohava.data.files.FileHelper;
import com.abohava.data.files.FileHelperImpl;
import com.abohava.data.prefs.PrefsHelper;
import com.abohava.data.prefs.PrefsHelperImpl;
import com.abohava.dependencies.ApplicationContext;
import com.abohava.dependencies.LooperScheduler;
import com.abohava.dependencies.WriteScheduler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.abohava.utils.Constants.BASE_URL;
import static com.abohava.utils.Constants.HANDLER_THREAD_NAME;

@Module
public class ApplicationModule {

    private final Application app;

    public ApplicationModule(Application app) {
        this.app = app;
    }

    @Provides
    @ApplicationContext
    public Context provideContext() {
        return app;
    }

    @Provides
    public Application provideApplication() {
        return app;
    }

    @Provides
    public Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<ApiHistoryWeather>>(){}.getType(), new WeatherDeserializer<ApiHistoryWeather>())                .registerTypeAdapter(new TypeToken<List<ApiCurrentWeather>>(){}.getType(), new WeatherDeserializer<ApiCurrentWeather>())
                .registerTypeAdapter(new TypeToken<List<ApiCurrentWeather>>(){}.getType(), new WeatherDeserializer<ApiCurrentWeather>())
                .registerTypeAdapter(new TypeToken<List<ApiForecastWeather>>(){}.getType(), new WeatherDeserializer<ApiForecastWeather>())
                .create();
    }

    @Provides
    public Retrofit provideRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(BASE_URL)
                .build();
    }

    @Provides
    public WeatherClient provideWeatherClient(Retrofit retrofit) {
        return retrofit.create(WeatherClient.class);
    }

    @Provides
    @Singleton
    public ApiHelper provideApiHelper(ApiHelperImpl apiHelperImpl) {
        return apiHelperImpl;
    }

    @Provides
    @LooperScheduler
    public Scheduler provideLooperScheduler() {
        HandlerThread handlerThread = new HandlerThread(HANDLER_THREAD_NAME);
        handlerThread.start();
        return AndroidSchedulers.from(handlerThread.getLooper());
    }

    @Provides
    @WriteScheduler
    public Scheduler provideWriteScheduler() {
        return Schedulers.from(Executors.newSingleThreadExecutor());
    }

    @Provides
    @Singleton
    public WeatherDbHelper provideWeatherDbHelper(WeatherDbHelperImpl weatherDbHelperImpl) {
        return weatherDbHelperImpl;
    }

    @Provides
    @Singleton
    public PrefsHelper providePrefsHelper(PrefsHelperImpl prefsHelperImpl) {
        return prefsHelperImpl;
    }

    @Provides
    @Singleton
    public FileHelper provideFileHelper(FileHelperImpl fileHelperImpl) {
        return fileHelperImpl;
    }

    @Provides
    @Singleton
    public DataManager provideDataManager(DataManagerImpl dataManagerImpl) {
        return dataManagerImpl;
    }
}
