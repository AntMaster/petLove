package com.shumahe.pethome.Util;

import javafx.util.converter.DateStringConverter;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    DateStringConverter dateStringConverter = new DateStringConverter();

    public static Date getStartTime(Date date) {
        Calendar todayStart = Calendar.getInstance();
        todayStart.setTime(date);
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    public static Date getEndTime(Date date) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTime(date);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    public static Date getNowStartTime() {
        Calendar ca = Calendar.getInstance();
        return getStartTime(ca.getTime());
    }

    public static Date getNowEndTime() {
        Calendar ca = Calendar.getInstance();
        return getEndTime(ca.getTime());
    }
}
