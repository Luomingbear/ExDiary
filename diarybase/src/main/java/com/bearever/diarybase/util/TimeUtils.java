package com.bearever.diarybase.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * luoming
 * 2019/3/30
 **/
public class TimeUtils {
    /**
     * 当前时间的的yyyy-MM-dd HH:mm:ss格式
     *
     * @return
     */
    public static String currentTime() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(date);
    }

    public static String getDiaryTime(String time) {
        StringBuilder result = new StringBuilder();
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        // 字符串转换日期格式
        DateFormat fmtDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = fmtDateTime.parse(time);
            calendar.setTime(date);
            result.append(calendar.get(Calendar.YEAR));
            result.append("年");
            result.append(calendar.get(Calendar.MONTH) + 1);
            result.append("月");
            result.append(calendar.get(Calendar.DAY_OF_MONTH));
            result.append("日");
            result.append("  ");

            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour < 1 || hour >= 23) {
                result.append("子时");
            } else if (hour <= 3) {
                result.append("丑时");
            } else if (hour <= 5) {
                result.append("寅时");
            } else if (hour <= 7) {
                result.append("卯时");
            } else if (hour <= 9) {
                result.append("辰时");
            } else if (hour <= 11) {
                result.append("巳时");
            } else if (hour <= 13) {
                result.append("午时");
            } else if (hour <= 15) {
                result.append("未时");
            } else if (hour <= 17) {
                result.append("申时");
            } else if (hour <= 19) {
                result.append("酉时");
            } else if (hour <= 21) {
                result.append("戊时");
            } else {
                result.append("亥时");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.append("未知时间");
        }
        return result.toString();
    }
}
