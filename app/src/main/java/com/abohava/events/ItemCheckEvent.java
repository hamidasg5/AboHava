package com.abohava.events;

public class ItemCheckEvent {

    private int mPosition;
    private boolean mIsChecked;

    public ItemCheckEvent(int position, boolean isChecked) {
        mPosition = position;
        mIsChecked = isChecked;
    }

    public int getPosition() {
        return mPosition;
    }

    public boolean isChecked() {
        return mIsChecked;
    }
}
