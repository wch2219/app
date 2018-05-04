package com.xxc.shoppingmall.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by XXC on 2017/11/26.
 * 时间转化工具类
 */
public class TimeUtils {

    public static String getThisMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM", Locale.getDefault());
        return format.format(new Date());
    }
    public static String getFormatDay(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(time);
    }
    public static String getFormatTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(time);
    }
    public static String getFormatMonth(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月", Locale.getDefault());
        return format.format(time);
    }

    public static String getThisYear() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy", Locale.getDefault());
        return format.format(new Date());
    }

    public static String getMonth() {
        SimpleDateFormat format = new SimpleDateFormat("MM", Locale.getDefault());
        return format.format(new Date());
    }
}
