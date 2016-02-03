package com.example.hi2.utils;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/2/3.
 */
public class DateUtils {
    public static boolean isCloseEnough(long var0, long var2) {
        long var4 = var0 - var2;
        if(var4 < 0L) {
            var4 = -var4;
        }

        return var4 < 30000L;
    }

    public static String getTime(String time){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(Long.parseLong(time));
        return dateString;
    }
}
