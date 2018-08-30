package com.abohava.base;

import android.support.annotation.StringRes;

public interface IView {

    void showToast(String message);

    void showToast(@StringRes int stringResId);

    void showSnackBar(String message);

    void showSnackBar(@StringRes int stringResId);
}
