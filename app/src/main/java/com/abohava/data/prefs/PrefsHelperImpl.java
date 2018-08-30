package com.abohava.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.abohava.R;
import com.abohava.dependencies.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PrefsHelperImpl implements PrefsHelper {

    private final Context mContext;
    private final SharedPreferences mPrefs;

    @Inject
    PrefsHelperImpl(@ApplicationContext Context context) {
        mContext = context;
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public boolean isAutoUpdateOn() {
        return mPrefs.getBoolean(mContext.getString(R.string.prefs_auto_update_weather), true);
    }

    @Override
    public String getUnits() {
        return mPrefs.getString(mContext.getString(R.string.prefs_units), "M");
    }

    @Override
    public int getPageIndex() {
        return mPrefs.getInt(mContext.getString(R.string.prefs_page_index), 0);
    }

    @Override
    public void setPageIndex(int index) {
        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putInt(mContext.getString(R.string.prefs_page_index), index);
        edit.apply();
    }
}
