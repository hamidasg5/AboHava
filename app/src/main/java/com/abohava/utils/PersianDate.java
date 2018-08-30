package com.abohava.utils;

import android.support.annotation.NonNull;

import java.util.Calendar;

public final class PersianDate implements Comparable<PersianDate> {

    private static final int[] correspondingMonth = {10, 11, 12, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    private static final int[] startDays = {11, 12, 10, 12, 11, 11, 10, 10, 10, 9, 10, 10};

    private long mMillis;
    private int mYear, mMonth, mDay, mDayOfWeek, mHour, mMinute, mSecond;

    public PersianDate() {
        fromGregorian(Calendar.getInstance());
    }

    public PersianDate(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        fromGregorian(calendar);
    }

    public PersianDate(Calendar calendar) {
        fromGregorian(calendar);
    }

    public PersianDate(PersianDate persianDate) {
        mMillis = persianDate.mMillis;
        mYear = persianDate.mYear;
        mMonth = persianDate.mMonth;
        mDay = persianDate.mDay;
        mDayOfWeek = persianDate.mDayOfWeek;
        mHour = persianDate.mHour;
        mMinute = persianDate.mMinute;
        mSecond = persianDate.mSecond;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PersianDate && ((PersianDate) obj).mMillis == mMillis;
    }

    @Override
    public int compareTo(@NonNull PersianDate o) {
        return Long.compare(mMillis, o.mMillis);
    }

    @Override
    public String toString()
    {
        return fullDateToString() + timeToString();
    }

    public String timeToString() {
        String strMinute = String.valueOf(mMinute);
        String strHour = String.valueOf(mHour);
        if (strMinute.length() == 1) {
            strMinute = "0" + strMinute;
        }
        if (strHour.length() == 1) {
            strHour = "0" + strHour;
        }
        return  strHour + ":" + strMinute;
    }

    public String getDayOfWeek() {
        switch (mDayOfWeek) {
            case 0: return "شنبه";
            case 1: return "یکشنبه";
            case 2: return "دوشنبه";
            case 3: return "سه شنبه";
            case 4: return "چهارشنبه";
            case 5: return "پنج شنبه";
            case 6: return "جمعه";
            default: return  null;
        }
    }

    public String dateToString() {
        String strDay = String.valueOf(mDay);
        String strMonth = String.valueOf(mMonth);
        String strYear = String.valueOf(mYear);
        if (strDay.length() == 1) {
            strDay = "0" + strDay;
        }
        if (strMonth.length() == 1) {
            strMonth = "0" + strMonth;
        }
        return strYear + "/" + strMonth + "/" + strDay;
    }

    public String fullDateToString() {
        return dateToString() + " " + getDayOfWeek();
    }

    public long getMillis() {
        return mMillis;
    }

    public int getYear() {
        return mYear;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }

    public int getHour() {
        return mHour;
    }

    public int getMinute() {
        return mMinute;
    }

    public int getSecond() {
        return mSecond;
    }

    private void fromGregorian(Calendar calendar) {
        mSecond = calendar.get(Calendar.SECOND);
        mMinute = calendar.get(Calendar.MINUTE);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) % 7;

        int day =  calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        if (month > 3) {
            mYear = year - 621;
        } else if (month < 3) {
            mYear = year - 622;
        } else {
            if (year % 4 == 0) {
                if (day >= 20) {
                    mYear = year - 621;
                } else {
                    mYear = year - 622;
                }
            } else {
                if (day >= 21) {
                    mYear = year - 621;
                } else {
                    mYear = year - 622;
                }
            }
        }

        mMonth = correspondingMonth[month - 1];

        int startDay = startDays[month - 1];
        if ((year % 4 == 0 && month > 2) || (year % 4 == 1 && month < 4)) {
            startDay++;
        }

        mDay = startDay + day - 1;

        int dayLimit = 31;
        if (mMonth > 6) {
            dayLimit = 30;
        }
        if (mMonth == 12 && year % 4 != 1) {
            dayLimit = 29;
        }
        if (mDay > dayLimit) {
            mDay = mDay - dayLimit;
            mMonth++;
            if (mMonth > 12) {
                mMonth = 1;
            }
        }
    }
}