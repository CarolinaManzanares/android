package com.carolinamanzanares.earthquakes;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

import com.carolinamanzanares.earthquakes.Fragments.SettingsFragment;
import com.carolinamanzanares.earthquakes.Managers.EarthquakeAlarmManager;

/**
 * Created by cursomovil on 26/03/15.
 */
public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();

        //	Register this OnSharedPreferenceChangeListener
        SharedPreferences prefs	= PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("EARTHQUAKE", "onSharedPreferenceChanged. key: " + key);

        Long frecuency;

        if (key.equals(getString(R.string.AUTOREFRESH))) {
            if (sharedPreferences.getBoolean(key, true)) {
                frecuency = Long.parseLong(sharedPreferences.getString(getString(R.string.FRECUENCY), "5")) * 60 * 1000;
                EarthquakeAlarmManager.setAlarm(this, frecuency);
            } else {
                EarthquakeAlarmManager.cancelAlarm(this);
            }
        } else if (key.equals(getString(R.string.FRECUENCY))) {
            frecuency = Long.parseLong(sharedPreferences.getString(key, "5")) * 60 * 1000;
            EarthquakeAlarmManager.updateAlarm(this, frecuency);
        }


    }


}
