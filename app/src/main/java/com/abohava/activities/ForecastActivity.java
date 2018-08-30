package com.abohava.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abohava.R;
import com.abohava.adapters.ForecastAdapter;
import com.abohava.base.BaseActivity;
import com.abohava.data.db.model.ForecastWeather;
import com.abohava.presenters.IForecastPresenter;
import com.abohava.utils.StringUtils;
import com.abohava.utils.UIUtils;
import com.abohava.views.PlotterView;

import java.util.List;

import javax.inject.Inject;

public class ForecastActivity extends BaseActivity implements IForecastActivity {

    private static final String TAG = "ForecastActivity";
    private static final String EXTRA_CITY_ID = "extra_city_id";
    private static final String EXTRA_CITY_NAME = "extra_city_name";
    private static final String EXTRA_CITY_INDEX = "extra_index";

    @Inject
    public LinearLayoutManager mLayoutManager;
    @Inject
    public ForecastAdapter mForecastAdapter;
    @Inject
    public IForecastPresenter<IForecastActivity> mPresenter;

    private ProgressBar mProgressBar;
    private RelativeLayout mContent;
    private PlotterView mPlotterView;

    public static Intent newIntent(Context context, int cityIndex, String cityId, String cityName) {
        Intent intent = new Intent(context, ForecastActivity.class);
        intent.putExtra(EXTRA_CITY_ID, cityId);
        intent.putExtra(EXTRA_CITY_NAME, cityName);
        intent.putExtra(EXTRA_CITY_INDEX, cityIndex);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_forecast);

        getActivityComponent().inject(this);

        int pageColor = getResources().getColor(UIUtils.getPageColor(getIntent().getIntExtra(EXTRA_CITY_INDEX, 0)));
        Toolbar toolbar = findViewById(R.id.forecast_toolbar);
        toolbar.setTitle(getIntent().getStringExtra(EXTRA_CITY_NAME));
        toolbar.setTitleTextColor(pageColor);
        Drawable backDrawable = getResources().getDrawable(R.drawable.ic_back);
        backDrawable.setTint(pageColor);
        toolbar.setNavigationIcon(backDrawable);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TextView tempGraphLabel = findViewById(R.id.forecast_temp_graph_label);
        tempGraphLabel.setText(StringUtils.toPersianDigits(getString(R.string.forecast_temp_graph_label)));

        TextView listLabel = findViewById(R.id.forecast_weather_list_label);
        listLabel.setText(StringUtils.toPersianDigits(getString(R.string.forecast_weather_list_label)));

        mProgressBar = findViewById(R.id.forecast_progress_bar);
        mContent = findViewById(R.id.forecast_content);
        mContent.setBackgroundColor(pageColor);

        mPlotterView = findViewById(R.id.forecast_temp_plotter);

        RecyclerView recyclerView = findViewById(R.id.forecast_recycler_view);
        recyclerView.setHasFixedSize(true);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mForecastAdapter);

        mPresenter.onAttach(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public String getCityId() {
        return getIntent().getStringExtra(EXTRA_CITY_ID);
    }

    @Override
    public void showForecastWeathers(List<ForecastWeather> forecastWeathers, String units) {
        mProgressBar.setVisibility(View.GONE);
        mContent.setVisibility(View.VISIBLE);
        mForecastAdapter.setUnits(units);
        mForecastAdapter.setItems(forecastWeathers);
    }

    @Override
    public void plotTemps(List<PointF> points, String units) {
        mPlotterView.setPoints(points, units);
    }
}
