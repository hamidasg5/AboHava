package com.abohava.utils;

import java.util.Calendar;
import java.util.TimeZone;

public class DateUtils {

    public static String[] getLast24Hours() {
        long time = System.currentTimeMillis();
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        today.setTimeInMillis(time);
        Calendar yesterday = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        yesterday.setTimeInMillis(time - 24 * 3600 * 1000);
        String startDate = getFormattedDate(yesterday);
        String endDate = getFormattedDate(today);
        return new String[] {startDate, endDate};
    }

    private static String getFormattedDate(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        String str = calendar.get(Calendar.YEAR) + "-";
        str += month < 10 ? "0" + month : month;
        str += "-";
        str += day < 10 ? "0" + day : day;
        str += ":";
        str += hour < 10 ? "0" + hour : hour;
        return str;
    }

    public static String toLocalTime(String gmt) {
        int rawOffset = TimeZone.getDefault().getRawOffset();
        rawOffset /= 1000;
        int offH = rawOffset / 3600 + 1;
        int offM = (rawOffset % 3600) / 60;
        int gmtH = Integer.parseInt(gmt.substring(0, 2));
        int gmtM = Integer.parseInt(gmt.substring(3));
        int locH = gmtH + offH;
        int locM = gmtM + offM;
        if (locM >= 60) {
            locH++;
            locM %= 60;
        }
        locH %= 24;
        String str = locH < 10 ? "0" + locH : String.valueOf(locH);
        str += ":";
        str += locM < 10 ? "0" + locM : locM;
        return str;
    }

    public static Long toLocalTimeStamp(Long timeStamp) {
        return (long) (timeStamp + 4.5 * 3600);
    }
}
