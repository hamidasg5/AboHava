package com.abohava;

import android.app.Application;
import android.preference.PreferenceManager;

import com.abohava.dependencies.components.ApplicationComponent;
import com.abohava.dependencies.components.DaggerApplicationComponent;
import com.abohava.dependencies.modules.ApplicationModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AboHavaApp extends Application {

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        //Realm.deleteRealm(configuration);
        Realm.setDefaultConfiguration(configuration);

        mApplicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
