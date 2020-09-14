package com.evsu.event.util;

import android.app.ActivityManager;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppUtil {

    public static String transformDate(String dateStr, String newFormat, String oldFormat) {
        SimpleDateFormat df = new SimpleDateFormat(newFormat, Locale.US);
        return df.format(strToDate(dateStr, oldFormat));
    }

    public static Date strToDate(String strDate, String dateFormat) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);
        try {
            return format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isServiceRunning(String serviceClass, Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

        // Notification service
        assert activityManager != null;
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

}
