package com.abohava.utils;

public class StringUtils {

    public static String translateWindDir(String windDir) {
        switch (windDir) {
            case "north": return "شمال";
            case "north-northeast": return "شمال-شمال شرقی";
            case "northeast": return "شمال شرقی";
            case "east-northeast": return "شرق-شمال شرقی";
            case "east": return "شرق";
            case "east-southeast": return "شرق-جنوب شرقی";
            case "southeast": return "جنوب شرقی";
            case "south-southeast": return "جنوب-جنوب شرقی";
            case "south": return "جنوب";
            case "south-southwest": return "جنوب-جنوب غربی";
            case "southwest": return "جنوب غربی";
            case "west-southwest": return "غرب-جنوب غربی";
            case "west": return "غرب";
            case "west-northwest": return "غرب-شمال غربی";
            case "northwest": return "شمال غربی";
            case "north-northwest": return "شمال-شمال غربی";
            default: return windDir;
        }
    }

    public static String translateDescription(String codeString) {
        int code = Integer.parseInt(codeString);
        switch (code){
            case 200: return "رعدوبرق و باران نم نم";
            case 201: return "رعدوبرق و باران";
            case 202: return "رعدوبرق و باران شدید";
            case 230: return "رعدوبرق و باران";
            case 231: return "رعدوبرق و باران";
            case 232: return "رعدوبرق و باران";
            case 233: return "رعدوبرق و تگرگ";
            case 300: return "بارش نم نم باران";
            case 301: return "بارش نم نم باران";
            case 302: return "بارش نم نم باران";
            case 500: return "بارش اندک باران";
            case 501: return "بارش باران";
            case 502: return "بارش شدید باران";
            case 511: return "بارش باران یخی";
            case 520: return "بارش شدید باران";
            case 521: return "بارش شدید باران";
            case 522: return "بارش شدید باران";
            case 600: return "بارش اندک برف";
            case 601: return "بارش برف";
            case 602: return "بارش شدید برف";
            case 610: return "برف و باران";
            case 611: return "بوران";
            case 612: return "بوران شدید";
            case 621: return "بارش سنگین برف";
            case 622: return "بارش سنگین برف";
            case 623: return "برف ناگهانی";
            case 700: return "مه آلود";
            case 711: return "دودی";
            case 721: return "مه آلود";
            case 731: return "گردوغبار";
            case 741: return "مه آلود";
            case 751: return "مه یخی";
            case 800: return "آسمان صاف";
            case 801: return "اندکی ابری";
            case 802: return "ابرهای پراکنده";
            case 803: return "پوشیده از ابر";
            case 804: return "ابرهای تیره";
            case 900: return "بارش نامعلوم";
            default: return "آسمان صاف";
        }
    }

    public static String toPersianDigits(String str) {
        char[] arabicChars = {'٠','١','٢','٣','٤','٥','٦','٧','٨','٩'};
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < str.length(); i++) {
            int index = (int)(str.charAt(i)) - 48;
            if(index > -1 && index < 10) {
                builder.append(arabicChars[index]);
            } else {
                builder.append(str.charAt(i));
            }
        }
        return builder.toString();
    }

    public static String getLastUpdateLabel(long date) {
        return toPersianDigits(new PersianDate(date).toString());
    }

    public static String getTempLabel(int temp, String units) {
        switch (units) {
            default:
            case "M": return toPersianDigits(" " + String.valueOf(temp) + "°");
            case "S": return toPersianDigits(" " + String.valueOf(temp + 273) + "°");
            case "I": return toPersianDigits(" " + String.valueOf((int) (temp * 1.8 + 32)) + "°");
        }
    }

    public static String getApparentTempLabel(int temp, String units) {
        return "دمای احساسی: " + getTempLabel(temp, units);
    }

    public static String getRelHumLabel(int rh) {
        return toPersianDigits("رطوبت نسبی: " + rh + "%");
    }

    public static String getCloudCovLabel(int cc) {
        return toPersianDigits("پوشش ابر: " + cc + "%");
    }

    public static String getWindSpeedLabel(int speed, String units) {
        switch (units) {
            default:
            case "M": return toPersianDigits(String.valueOf((int) (speed * 3.6))) + "کیلومتربرساعت";
            case "S": return toPersianDigits(String.valueOf(speed)) + "متربرثانیه";
            case "I": return toPersianDigits(String.valueOf((int) (speed * 2.23694))) + "مایل برساعت";
        }
    }

    public static String getWindDirLabel(String direction) {
        return direction;
    }

    public static String getSunriseLabel(String sunrise) {
        return toPersianDigits("طلوع خورشید: " + sunrise);
    }

    public static String getSunsetLabel(String sunset) {
        return toPersianDigits("غروب خورشید: " + sunset);
    }

    public static String getUVLabel(float uv) {
        return toPersianDigits("ماورابنفش: " + uv);
    }

    public static String getDailyDateLabel(long time) {
        return new PersianDate(time * 1000).getDayOfWeek();
    }

    public static String getHourLabel(String hour) {
        return toPersianDigits(hour);
    }

    public static String getPProbText(int pp) {
        return toPersianDigits("احتمال بارش: " + pp + "%");
    }
}
