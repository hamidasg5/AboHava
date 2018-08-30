package com.abohava.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.abohava.R;

public class YNDialogFragment extends DialogFragment {

    private static final String ARG_TITLE = "argTitle";
    private static final String ARG_MESSAGE = "argMessage";
    private static final String ARG_NEG_LABEL = "argNegLabel";
    private static final String ARG_POS_LABEL = "argPosLabel";

    private Context mContext;

    public static YNDialogFragment newInstance(
            String title,
            @NonNull String message,
            String negLabel,
            String posLabel)
    {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_NEG_LABEL, negLabel);
        args.putString(ARG_POS_LABEL, posLabel);
        YNDialogFragment dialogFragment = new YNDialogFragment();
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        mContext = null;
        super.onDetach();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_fragment_yes_no, null);

        final TextView titleTextView = view.findViewById(R.id.dialog_yes_no_title);
        final TextView messageTextView = view.findViewById(R.id.dialog_yes_no_message);
        final Button negativeButton = view.findViewById(R.id.dialog_yes_no_btn_negative);
        final Button positiveButton = view.findViewById(R.id.dialog_yes_no_btn_positive);

        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString(ARG_TITLE);
            String message = args.getString(ARG_MESSAGE);
            String negativeLabel = args.getString(ARG_NEG_LABEL);
            String positiveLabel = args.getString(ARG_POS_LABEL);

            titleTextView.setText(title==null ? getString(R.string.warning) : title);
            messageTextView.setText(message);
            negativeButton.setText(negativeLabel==null ? getString(R.string.cancel) : negativeLabel);
            positiveButton.setText(positiveLabel==null ? getString(R.string.ok) : positiveLabel);
        }

        negativeButton.setOnClickListener(v -> {
            ResponseCallback callback = (ResponseCallback) mContext;
            callback.onDismiss(false, getTag());
            dismiss();
        });
        positiveButton.setOnClickListener(v -> {
            ResponseCallback callback = (ResponseCallback) mContext;
            callback.onDismiss(true, getTag());
            dismiss();
        });

        return new AlertDialog.Builder(mContext).setView(view).show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public interface ResponseCallback {

        void onDismiss(boolean response, String tag);
    }
}