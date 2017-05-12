package com.example.android.notepad;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil
{
    private static String defaultDatePattern = "yyyy-MM-dd";

    private static String defaultTimeStampPattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * 返回默认格式的当前日期
     *
     * @return String
     */
    public static String getToday()
    {
        Date today = new Date();
        return convertDateToString(today,defaultDatePattern);
    }

    /**
     * 返回默认格式的当前时间戳字符串格式
     *
     * @return String
     */
    public static String getTodayTimeStampString()
    {
        Date today = new Date();
        return convertDateToString(today, defaultTimeStampPattern);
    }

    /**
     * 使用默认格式转换Date成字符串
     *
     * @param date
     * @return String
     */
    public static String convertDateToString(Date date)
    {
        return convertDateToString(date, defaultDatePattern);
    }

    /**
     * 使用指定格式转换Date成字符串
     *
     * @param date
     * @param pattern
     * @return String
     */
    public static String convertDateToString(Date date, String pattern)
    {
        String returnValue = "";

        if (date != null)
        {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return returnValue;
    }
}
