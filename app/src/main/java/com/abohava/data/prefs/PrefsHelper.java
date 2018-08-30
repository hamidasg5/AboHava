package com.abohava.data.prefs;

public interface PrefsHelper {

    boolean isAutoUpdateOn();

    String getUnits();

    int getPageIndex();

    void setPageIndex(int index);
}
