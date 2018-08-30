package com.abohava.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.abohava.R;
import com.abohava.base.BaseActivity;
import com.abohava.data.db.model.City;
import com.abohava.data.db.model.CurrentWeather;
import com.abohava.data.db.model.ForecastWeather;
import com.abohava.presenters.IWeatherPresenter;
import com.abohava.utils.StringUtils;
import com.abohava.utils.UIUtils;
import com.abohava.views.PlotterView;
import com.rd.PageIndicatorView;

import java.util.List;

import javax.inject.Inject;

public class WeatherActivity extends BaseActivity implements IWeatherActivity {

    private static final String TAG = "WeatherActivity";

    private static final int REQUEST_CITY_ID = 0;

    private ConstraintLayout mRootView;
    private TextView mCityNameText;
    private PageIndicatorView mPageIndicator;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mRefreshLayout;
    private ConstraintLayout mContent;
    private ImageView mBackgroundImage;
    private ImageView mWeatherIcon;
    private TextView mWeatherDescription;
    private TextView mCurrentTemp;
    private TextView mApparentTemp;
    private TextView mLastUpdateText;
    private PlotterView mTempPlotter;
    private TextView mForecastDate01;
    private TextView mForecastDate02;
    private TextView mForecastDate03;
    private TextView mForecastTemp01;
    private TextView mForecastTemp02;
    private TextView mForecastTemp03;
    private ImageView mForecastIcon01;
    private ImageView mForecastIcon02;
    private ImageView mForecastIcon03;
    private TextView mRelativeHumidity;
    private TextView mCloudCoverage;
    private TextView mWindSpeed;
    private TextView mWindDirection;
    private TextView mSunrise;
    private TextView mSunset;
    private TextView mUV;

    @Inject
    public IWeatherPresenter<IWeatherActivity> mPresenter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_weather);

        getActivityComponent().inject(this);

        mRefreshLayout = findViewById(R.id.scroll_view_refresh);
        mRefreshLayout.setOnRefreshListener(() -> mPresenter.onRefresh());
        mRefreshLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (mCityNameText != null) {
                    mCityNameText.setPadding(0, 0, 0, 0);
                    mCityNameText.setAlpha(1.0f);
                }
            }
            return false;
        });
        ScrollView scrollView = findViewById(R.id.scroll_view);
        scrollView.setOnTouchListener(new SwipeListener());
        ImageButton cityListButton = findViewById(R.id.city_list_btn);
        cityListButton.setOnClickListener(v -> startActivityForResult(new Intent(WeatherActivity.this, CityActivity.class), REQUEST_CITY_ID));
        ImageButton settingsButton = findViewById(R.id.settings_btn);
        settingsButton.setOnClickListener(v -> startActivity(new Intent(WeatherActivity.this, SettingsActivity.class)));
        Button historyBtn = findViewById(R.id.history_weather_btn);
        historyBtn.setOnClickListener(v -> startActivity(HistoryActivity.newIntent(
                WeatherActivity.this,
                mPresenter.getPageIndex(),
                mPresenter.getCityId(),
                mPresenter.getCityName())));
        Button forecastBtn = findViewById(R.id.forecast_weather_btn);
        forecastBtn.setOnClickListener(v -> startActivity(ForecastActivity.newIntent(
                WeatherActivity.this,
                mPresenter.getPageIndex(),
                mPresenter.getCityId(),
                mPresenter.getCityName())));

        mRootView = findViewById(R.id.home_root_view);
        mCityNameText = findViewById(R.id.city_name_label);
        mPageIndicator = findViewById(R.id.page_indicator);
        mProgressBar = findViewById(R.id.home_progress_bar);
        mContent = findViewById(R.id.current_weather_content);
        mBackgroundImage = findViewById(R.id.city_background_image);
        mWeatherIcon = findViewById(R.id.weather_icon);
        mWeatherDescription = findViewById(R.id.weather_description);
        mCurrentTemp = findViewById(R.id.current_temp);
        mApparentTemp = findViewById(R.id.apparent_temp);
        mLastUpdateText = findViewById(R.id.last_update_date_text);
        mTempPlotter = findViewById(R.id.current_temp_plotter);
        mForecastDate01 = findViewById(R.id.forecastDay01_date);
        mForecastDate02 = findViewById(R.id.forecastDay02_date);
        mForecastDate03 = findViewById(R.id.forecastDay03_date);
        mForecastTemp01 = findViewById(R.id.forecastDay01_temp);
        mForecastTemp02 = findViewById(R.id.forecastDay02_temp);
        mForecastTemp03 = findViewById(R.id.forecastDay03_temp);
        mForecastIcon01 = findViewById(R.id.forecastDay01_icon);
        mForecastIcon02 = findViewById(R.id.forecastDay02_icon);
        mForecastIcon03 = findViewById(R.id.forecastDay03_icon);
        mRelativeHumidity = findViewById(R.id.group_humidity_rh);
        mCloudCoverage = findViewById(R.id.group_humidity_cloud);
        mWindSpeed = findViewById(R.id.group_wind_speed);
        mWindDirection = findViewById(R.id.group_wind_direction);
        mSunrise = findViewById(R.id.group_sun_rise);
        mSunset = findViewById(R.id.group_sun_set);
        mUV = findViewById(R.id.group_sun_uv);

        mPresenter.onAttach(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CITY_ID && resultCode == RESULT_OK) {
            String cityID = data.getStringExtra(CityActivity.EXTRA_CITY_ID);
            if (cityID != null) {
                mPresenter.setPage(cityID);
            }
        }
    }

    @Override
    public void showCityCount(int count) {
        mPageIndicator.setCount(count);
    }

    @Override
    public void showCurrentWeather(int index, City city, String units) {
        updatePage(index);
        CurrentWeather currentWeather = city.getCurrentWeather();
        mCityNameText.setText(city.getName());
        mWeatherIcon.setImageResource(UIUtils.getWeatherIconRes(currentWeather.getWeatherIcon()));
        mWeatherDescription.setText(StringUtils.translateDescription(currentWeather.getWeatherCode()));
        mCurrentTemp.setText(StringUtils.getTempLabel(currentWeather.getTemp(), units));
        mApparentTemp.setText(StringUtils.getApparentTempLabel(currentWeather.getApparentTemp(), units));
        mLastUpdateText.setText(StringUtils.getLastUpdateLabel(city.getCurrentLastUpdate()));
        mRelativeHumidity.setText(StringUtils.getRelHumLabel(currentWeather.getRelativeHumidity()));
        mCloudCoverage.setText(StringUtils.getCloudCovLabel(currentWeather.getCloudCoverage()));
        mWindSpeed.setText(StringUtils.getWindSpeedLabel(currentWeather.getWindSpeed(), units));
        mWindDirection.setText(StringUtils.getWindDirLabel(currentWeather.getWindDirection()));
        mSunrise.setText(StringUtils.getSunriseLabel(currentWeather.getSunrise()));
        mSunset.setText(StringUtils.getSunsetLabel(currentWeather.getSunset()));
        mUV.setText(StringUtils.getUVLabel(currentWeather.getUV()));
    }

    @Override
    public void showDailyWeathers(int index, City city, String units) {
        updatePage(index);
        List<ForecastWeather> forecastDailyWeathers = city.getForecastDailyWeathers();
        ForecastWeather day01 = forecastDailyWeathers.get(1);
        if (day01 != null) {
            mForecastDate01.setText(StringUtils.getDailyDateLabel(day01.getTimeStamp()));
            mForecastIcon01.setImageResource(UIUtils.getWeatherIconRes(day01.getWeatherIcon()));
            mForecastTemp01.setText(StringUtils.getTempLabel(day01.getTemp(), units));
        }
        ForecastWeather day02 = forecastDailyWeathers.get(2);
        if (day02 != null) {
            mForecastDate02.setText(StringUtils.getDailyDateLabel(day02.getTimeStamp()));
            mForecastIcon02.setImageResource(UIUtils.getWeatherIconRes(day02.getWeatherIcon()));
            mForecastTemp02.setText(StringUtils.getTempLabel(day02.getTemp(), units));
        }
        ForecastWeather day03 = forecastDailyWeathers.get(3);
        if (day03 != null) {
            mForecastDate03.setText(StringUtils.getDailyDateLabel(day03.getTimeStamp()));
            mForecastIcon03.setImageResource(UIUtils.getWeatherIconRes(day03.getWeatherIcon()));
            mForecastTemp03.setText(StringUtils.getTempLabel(day03.getTemp(), units));
        }
    }

    @Override
    public void plotTemps(List<PointF> points, String units) {
        mTempPlotter.setPoints(points, units);
    }

    private void updatePage(int index) {
        mRefreshLayout.setRefreshing(false);
        mProgressBar.setVisibility(View.GONE);
        mContent.setVisibility(View.VISIBLE);
        mPageIndicator.setSelected(index);
        getWindow().setStatusBarColor(getResources().getColor(UIUtils.getPageColor(index)));
        mRootView.setBackgroundColor(getResources().getColor(UIUtils.getPageColor(index)));
        mBackgroundImage.setImageResource(UIUtils.getBackground(index));
    }

    private class SwipeListener implements View.OnTouchListener {

        private static final float CLICK_THRESHOLD = 10.0f;
        private final float SWIPE_THRESHOLD;
        private final float HORIZ_PADDING_MAX;

        private float mDownX;
        private float mDownY;

        SwipeListener() {
            int widthPixels = getResources().getDisplayMetrics().widthPixels;
            SWIPE_THRESHOLD = widthPixels * 0.35f;
            HORIZ_PADDING_MAX = widthPixels * 0.35f;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    mDownX = event.getX();
                    mDownY = event.getY();
                    return false;
                }
                case MotionEvent.ACTION_MOVE: {
                    float diffX = event.getX() - mDownX;
                    float ratioX = Math.abs(diffX) / SWIPE_THRESHOLD;
                    ratioX = ratioX > 1.0f ? 1.0f : ratioX;
                    if (diffX < 0.0f) {
                        mCityNameText.setPadding(0, 0, (int) (HORIZ_PADDING_MAX * ratioX * ratioX), 0);
                        mCityNameText.setAlpha(1.2f - ratioX);
                    } else {
                        mCityNameText.setPadding((int) (HORIZ_PADDING_MAX * ratioX * ratioX), 0, 0, 0);
                        mCityNameText.setAlpha(1.2f - ratioX);
                    }
                    return false;
                }
                case MotionEvent.ACTION_UP: {
                    float diffX = event.getX() - mDownX;
                    float diffY = event.getY() - mDownY;
                    mCityNameText.setPadding(0, 0, 0, 0);
                    mCityNameText.setAlpha(1.0f);
                    if (Math.abs(diffX) <  CLICK_THRESHOLD && Math.abs(diffY) <  CLICK_THRESHOLD) {
                        return v.performClick();
                    } else if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(diffY) < SWIPE_THRESHOLD * 0.65f) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mContent.setVisibility(View.GONE);
                        if (diffX > 0) {
                            mPresenter.onSwipeRight();
                        } else {
                            mPresenter.onSwipeLeft();
                        }
                        return true;
                    }
                    return false;
                }
                default:
                    return false;
            }
        }
    }
}
