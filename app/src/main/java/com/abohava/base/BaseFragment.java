package com.abohava.base;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.abohava.dependencies.components.ActivityComponent;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseFragment extends Fragment implements IView {

    private BaseActivity mBaseActivity;
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseActivity = (BaseActivity) context;
        mBaseActivity.onFragmentAttached(getTag());
    }

    @Override
    public void onDetach() {
        mBaseActivity.onFragmentDetached(getTag());
        mBaseActivity = null;
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        super.onDestroyView();
    }

    public ActivityComponent getActivityComponent() {
        if (mBaseActivity != null) {
            return mBaseActivity.getActivityComponent();
        }
        return null;
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
        Toast.makeText(mBaseActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int stringResId) {
        showToast(getString(stringResId));
    }

    @Override
    public void showSnackBar(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showSnackBar(int stringResId) {
        showSnackBar(getString(stringResId));
    }

    public interface Callback {

        void onFragmentAttached(String tag);

        void onFragmentDetached(String tag);
    }
}
