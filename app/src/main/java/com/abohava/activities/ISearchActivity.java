package com.abohava.activities;

import com.abohava.base.IView;

import java.util.List;

public interface ISearchActivity extends IView {

    void showCityItems(List<String> items);
}
