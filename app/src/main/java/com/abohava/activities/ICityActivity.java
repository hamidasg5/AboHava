package com.abohava.activities;

import com.abohava.base.IView;
import com.abohava.data.db.model.City;

import java.util.List;

public interface ICityActivity extends IView {

    void showCityItems(List<City> items);
}
