package com.abohava.utils;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;

public class ParcelableSparseBooleanArray extends SparseBooleanArray implements Parcelable {

    public static final Creator<ParcelableSparseBooleanArray> CREATOR = new Creator<ParcelableSparseBooleanArray>() {
        @Override
        public ParcelableSparseBooleanArray createFromParcel(Parcel in) {
            return new ParcelableSparseBooleanArray(in);
        }

        @Override
        public ParcelableSparseBooleanArray[] newArray(int size) {
            return new ParcelableSparseBooleanArray[size];
        }
    };

    public ParcelableSparseBooleanArray() {
        super();
    }

    @SuppressLint("ParcelClassLoader")
    private ParcelableSparseBooleanArray(Parcel in) {
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            put(in.readInt(), (Boolean) in.readValue(null));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(size());
        for (int i = 0; i < size(); i++) {
            dest.writeInt(keyAt(i));
            dest.writeValue(valueAt(i));
        }
    }
}