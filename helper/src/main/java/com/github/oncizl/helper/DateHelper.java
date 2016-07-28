package com.github.oncizl.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    public final static String yMdHms = "yyyy-MM-dd HH:mm:ss";
    public final static String yMdHm = "yyyy-MM-dd HH:mm";
    public final static String yMd = "yyyy-MM-dd";
    public final static String yM = "yyyy-MM";
    public final static String Md = "MM-dd";
    public final static String MdHm = "MM-dd HH:mm";
    public final static String Hms = "HH:mm:ss";
    public final static String Hm = "HH:mm";

    private DateHelper() {
    }

    public static String string(long time, String pattern) {
        return string(time, pattern, null);
    }

    public static String string(long time, String pattern, Locale locale) {
        if (locale == null) locale = Locale.getDefault();
        return new SimpleDateFormat(pattern, locale).format(new Date(time));
    }

    public static long time(String datetime, String pattern) {
        return time(datetime, pattern, null);
    }

    public static long time(String datetime, String pattern, Locale locale) {
        try {
            if (locale == null) locale = Locale.getDefault();
            return new SimpleDateFormat(pattern, locale).parse(datetime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String string2(String datetime, String pattern) {
        return string2(datetime, pattern, null);
    }

    public static String string2(String datetime, String pattern, Locale locale) {
        return string(time(datetime, pattern, locale), pattern, locale);
    }

    public static long time2(long time, String pattern) {
        return time2(time, pattern, null);
    }

    public static long time2(long time, String pattern, Locale locale) {
        return time(string(time, pattern, locale), pattern, locale);
    }
}
