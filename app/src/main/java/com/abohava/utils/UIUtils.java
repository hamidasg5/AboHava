package com.abohava.utils;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.abohava.R;

public class UIUtils {

    @ColorRes
    public static int getPageColor(int index) {
        switch (index % 10) {
            case 0: return R.color.color00;
            case 1: return R.color.color01;
            case 2: return R.color.color02;
            case 3: return R.color.color03;
            case 4: return R.color.color04;
            case 5: return R.color.color05;
            case 6: return R.color.color06;
            case 7: return R.color.color07;
            case 8: return R.color.color08;
            case 9: return R.color.color09;
            default: return R.color.color00;
        }
    }

    @DrawableRes
    public static int getBackground(int index) {
        switch (index % 10) {
            case 0: return R.drawable.ic_background_00;
            case 1: return R.drawable.ic_background_01;
            case 2: return R.drawable.ic_background_02;
            case 3: return R.drawable.ic_background_03;
            case 4: return R.drawable.ic_background_04;
            case 5: return R.drawable.ic_background_05;
            case 6: return R.drawable.ic_background_06;
            case 7: return R.drawable.ic_background_07;
            case 8: return R.drawable.ic_background_08;
            case 9: return R.drawable.ic_background_09;
            default: return R.drawable.ic_background_00;
        }
    }

    @DrawableRes
    public static int getWeatherIconRes(String code) {
        switch (code) {
            case "t01d":
            case "t02d":
            case "t03d":
            case "t01n":
            case "t02n":
            case "t03n": return R.drawable.ic_clouds_rain_thunder;
            case "t04d":
            case "t05d":
            case "t04n":
            case "t05n": return R.drawable.ic_clouds_thunder;
            case "s01d":
            case "s01n":
            case "s02d":
            case "s02n":
            case "s03d":
            case "s03n":
            case "s04d":
            case "s04n":
            case "s05d":
            case "s05n":
            case "s06d":
            case "s06n":
            case "d01d":
            case "d02d":
            case "d03d":
            case "d01n":
            case "d02n":
            case "d03n": return R.drawable.ic_clouds_snow;
            case "r01d":
            case "r02d":
            case "r03d":
            case "r01n":
            case "r02n":
            case "r03n":
            case "r04d":
            case "r04n":
            case "r05d":
            case "r05n":
            case "r06d":
            case "r06n":
            case "f01d":
            case "f01n":
            case "u00d":
            case "u00n": return R.drawable.ic_clouds_rain;
            case "a01d":
            case "a02d":
            case "a03d":
            case "a04d":
            case "a05d":
            case "a06d":
            case "a01n":
            case "a02n":
            case "a03n":
            case "a04n":
            case "a05n":
            case "a06n": return R.drawable.ic_clouds_mist;
            case "c01d": return R.drawable.ic_sun;
            case "c01n": return R.drawable.ic_moon;
            case "c02d":
            case "c03d": return R.drawable.ic_clouds_sun;
            case "c02n":
            case "c03n": return R.drawable.ic_clouds_moon;
            case "c04d":
            case "c04n": return R.drawable.ic_clouds;
            default: return R.drawable.ic_sun;
        }
    }
}
