package com.abohava.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.abohava.R;
import com.abohava.base.BaseActivity;
import com.abohava.utils.Constants;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends BaseActivity {

    private static final String TAG = "MapActivity";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";

    private static final int REQUEST_PERMISSION_LOCATION = 0;

    private LatLng mCenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_map);

        Toolbar toolbar = findViewById(R.id.map_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (requestPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                getString(R.string.location_permission_message),
                REQUEST_PERMISSION_LOCATION)) {
            SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
            setupGoogleMap(fragment, savedInstanceState==null, true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_ok) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_LATITUDE, getValidLatLon(mCenter.latitude));
            intent.putExtra(EXTRA_LONGITUDE, getValidLatLon(mCenter.longitude));
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            SupportMapFragment fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupGoogleMap(fragment, true, true);
            } else {
                setupGoogleMap(fragment, true, false);
                showToast(getString(R.string.location_access_denied));
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void setupGoogleMap(SupportMapFragment mapFragment, boolean needsInit, boolean locationEnabled) {
        mapFragment.getMapAsync(googleMap -> {
            if (needsInit) {
                LatLng latLng = new LatLng(Constants.TEHRAN_LATITUDE, Constants.TEHRAN_LONGITUDE);
                CameraPosition cameraPosition = new CameraPosition(latLng, 12, 1, 1);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                googleMap.animateCamera(cameraUpdate);
            }
            googleMap.setOnCameraMoveListener(() -> mCenter = googleMap.getCameraPosition().target);
            if (locationEnabled) {
                googleMap.setMyLocationEnabled(true);
            }
        });
    }

    private double getValidLatLon(double value) {
        return ((double)Math.round(value * 10000d)) / 10000d;
    }
}
