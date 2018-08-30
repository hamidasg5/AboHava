package com.abohava.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;

import com.abohava.R;
import com.abohava.adapters.CityAdapter;
import com.abohava.base.BaseActivity;
import com.abohava.data.db.model.City;
import com.abohava.fragments.YNDialogFragment;
import com.abohava.presenters.ICityPresenter;
import com.abohava.utils.Constants;
import com.abohava.views.HorizontalDividerItemDecoration;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class CityActivity extends BaseActivity implements ICityActivity, YNDialogFragment.ResponseCallback {

    private static final String TAG = "CityActivity";
    private static final String DIALOG_DELETE_CITY = "dialog_delete_city";
    private static final String STATE_PENDING_DELETE = "state_pending_delete";
    public static final String EXTRA_CITY_ID = "extra_city_id";

    private static final int REQUEST_CITY_NAME = 0;
    private static final int REQUEST_LATLON = 1;

    private String mCityPendingDelete;

    @Inject
    public ICityPresenter<ICityActivity> mPresenter;
    @Inject
    public LinearLayoutManager mLayoutManager;
    @Inject
    public CityAdapter mCityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_city);

        getActivityComponent().inject(this);

        if (savedInstanceState != null) {
            mCityAdapter.onRestoreInstanceState(savedInstanceState);
            mCityPendingDelete = savedInstanceState.getString(STATE_PENDING_DELETE);
        }

        Toolbar toolbar = findViewById(R.id.city_toolbar);
        Drawable backDrawable = getResources().getDrawable(R.drawable.ic_back);
        backDrawable.setTint(getResources().getColor(R.color.colorTextPrimary));
        toolbar.setNavigationIcon(backDrawable);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.city_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        Drawable drawable = getResources().getDrawable(R.drawable.list_divider_drawable);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration(drawable));
        recyclerView.setAdapter(mCityAdapter);

        getCompositeDisposable().add(mCityAdapter.getItemDeletes()
                .throttleFirst(1, TimeUnit.SECONDS) // prevents opening two dialogs when user fast clicks the button
                .subscribe(cityName -> {
                    mCityPendingDelete = cityName;
                    YNDialogFragment dialogFragment = YNDialogFragment.newInstance(
                            null,
                            getString(R.string.city_delete_message, cityName),
                            null,
                            null);
                    dialogFragment.show(getSupportFragmentManager(), DIALOG_DELETE_CITY);
                })
        );

        getCompositeDisposable().add(mCityAdapter.getItemClicks()
                .subscribe(position -> {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_CITY_ID, mCityAdapter.getItems().get(position).getId());
                    setResult(RESULT_OK, intent);
                    finish();
                })
        );

        Button addFromListBtn = findViewById(R.id.add_from_list);
        addFromListBtn.setOnClickListener(v -> startActivityForResult(new Intent(CityActivity.this, SearchActivity.class), REQUEST_CITY_NAME));
        Button addFromMapBtn = findViewById(R.id.add_from_map);
        addFromMapBtn.setOnClickListener(v -> startActivityForResult(new Intent(CityActivity.this, MapActivity.class), REQUEST_LATLON));

        mPresenter.onAttach(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCityAdapter.onSaveInstanceState(outState);
        outState.putString(STATE_PENDING_DELETE, mCityPendingDelete);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CITY_NAME == requestCode && RESULT_OK == resultCode) {
            String cityName = data.getStringExtra(SearchActivity.EXTRA_CITY_NAME);
            mPresenter.addCity(cityName);
        } else if (REQUEST_LATLON == requestCode && RESULT_OK == resultCode) {
            double latitude = data.getDoubleExtra(MapActivity.EXTRA_LATITUDE, Constants.TEHRAN_LATITUDE);
            double longitude = data.getDoubleExtra(MapActivity.EXTRA_LONGITUDE, Constants.TEHRAN_LONGITUDE);
            mPresenter.addCity(latitude, longitude);
        }
    }

    @Override
    public void showCityItems(List<City> items) {
        mCityAdapter.setItems(items);
    }

    @Override
    public void onDismiss(boolean response, String tag) {
        if (tag.equals(DIALOG_DELETE_CITY)) {
            if (mCityPendingDelete != null){
                if (response) {
                    mPresenter.deleteCityByName(mCityPendingDelete);
                    mCityPendingDelete = null;
                } else {
                    mCityPendingDelete = null;
                }
            }
        }
    }
}
