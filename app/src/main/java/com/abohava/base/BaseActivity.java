package com.abohava.base;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.abohava.AboHavaApp;
import com.abohava.dependencies.components.ActivityComponent;
import com.abohava.dependencies.components.DaggerActivityComponent;
import com.abohava.dependencies.modules.ActivityModule;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity implements IView, BaseFragment.Callback {

    private ActivityComponent mActivityComponent;
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivityComponent = DaggerActivityComponent
                .builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((AboHavaApp)(getApplication())).getApplicationComponent())
                .build();
    }

    @Override
    protected void onDestroy() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

    public boolean requestPermission(String permission, @Nullable String message, int request_code) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (message != null && ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showSnackBar(message);
            }

            ActivityCompat.requestPermissions(this, new String[] {permission}, request_code);

            return false;
        }
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    @Inject
    public void setCompositeDisposable(CompositeDisposable compositeDisposable) {
        mCompositeDisposable = compositeDisposable;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int stringResId) {
        showToast(getString(stringResId));
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showSnackBar(int stringResId) {
        showSnackBar(getString(stringResId));
    }

    @Override
    public void onFragmentAttached(String tag) {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }
}
