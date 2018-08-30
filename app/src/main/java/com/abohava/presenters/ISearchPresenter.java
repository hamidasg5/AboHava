package com.abohava.presenters;

import com.abohava.activities.ISearchActivity;
import com.abohava.base.IPresenter;

public interface ISearchPresenter<V extends ISearchActivity> extends IPresenter<V> {

    void searchCities(String query);
}
