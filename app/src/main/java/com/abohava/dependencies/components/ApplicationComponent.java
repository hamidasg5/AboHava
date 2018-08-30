package com.abohava.dependencies.components;

import android.app.Application;
import android.content.Context;

import com.abohava.AboHavaApp;
import com.abohava.data.DataManager;
import com.abohava.dependencies.ApplicationContext;
import com.abohava.dependencies.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(AboHavaApp app);

    @ApplicationContext
    Context getContext();

    Application getApplication();

    DataManager getDataManager();
}
