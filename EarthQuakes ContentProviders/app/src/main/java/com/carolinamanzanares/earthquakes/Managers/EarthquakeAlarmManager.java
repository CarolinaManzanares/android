package com.carolinamanzanares.earthquakes.Managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.carolinamanzanares.earthquakes.Services.DownloadEarthquakesService;

/**
 * Created by cursomovil on 1/04/15.
 */
public class EarthquakeAlarmManager {

    public static void setAlarm(Context context, long interval) {

        //Get a	reference to the Alarm Manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //Set the alarm	type.
        int alarmType = AlarmManager.RTC;

        //Create a Pending Intent that will	broadcast and action
        Intent intentToFire = new Intent(context, DownloadEarthquakesService.class);
        PendingIntent alarmIntent = PendingIntent.getService(context, 0, intentToFire, 0);

        alarmManager.setRepeating(alarmType, interval, interval, alarmIntent);
    }

    public static void cancelAlarm(Context context) {

        //Get a	reference to the Alarm Manager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //Create	a	Pending	Intent	that	will	broadcast	and	action
        Intent intentToFire = new Intent(context, DownloadEarthquakesService.class);
        PendingIntent alarmIntent = PendingIntent.getService(context, 0, intentToFire, 0);

        alarmManager.cancel(alarmIntent);


    }

    public static void updateAlarm(Context context, long interval) {
        setAlarm(context, interval);
    }
}
