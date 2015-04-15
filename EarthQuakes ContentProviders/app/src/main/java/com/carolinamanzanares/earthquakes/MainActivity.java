package com.carolinamanzanares.earthquakes;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.carolinamanzanares.earthquakes.Fragments.EarthQuakeListFragment;
import com.carolinamanzanares.earthquakes.Fragments.EarthquakeMapFragment;
import com.carolinamanzanares.earthquakes.Listeners.TabListener;
import com.carolinamanzanares.earthquakes.Managers.EarthquakeAlarmManager;
import com.carolinamanzanares.earthquakes.Services.DownloadEarthquakesService;
import com.carolinamanzanares.earthquakes.task.DownloadEarthquakesTask;


public class MainActivity extends Activity implements DownloadEarthquakesTask.AddEarthQuakeInterface{

    private static final int PREFS_ACTIVITY = 1;
    private static final String EARTHQUAKES_PREFS = "EARTHQUAKES_PREFS";
    private static final String SELECTED_TAB = "SELECTED_TAB";
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Tabs
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab listTab = actionBar.newTab()
                .setText(R.string.List)
                .setTabListener(new TabListener<EarthQuakeListFragment>(
                        this, R.id.main_frame, EarthQuakeListFragment.class));
        actionBar.addTab(listTab);

        ActionBar.Tab mapTab = actionBar.newTab()
                .setText(R.string.Map)
                .setTabListener(new TabListener<EarthquakeMapFragment>(
                        this, R.id.main_frame, EarthquakeMapFragment.class));
        actionBar.addTab(mapTab);

        checkToSetAlarm();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SELECTED_TAB, actionBar.getSelectedNavigationIndex());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        actionBar.setSelectedNavigationItem(savedInstanceState.getInt(SELECTED_TAB));
    }

    private void checkToSetAlarm() {

        String KEY = "LAUNCHED_BEFORE";

        SharedPreferences prefs = getSharedPreferences(EARTHQUAKES_PREFS, Activity.MODE_PRIVATE);
        if(!prefs.getBoolean(KEY, false)){
            //es la primera vez. Entonces: cambiar alarma..
            long interval = getResources().getInteger(R.integer.default_interval) * 60 * 1000;
            EarthquakeAlarmManager.setAlarm(this, interval);

            //..y establecer el flag
            prefs.edit().putBoolean(KEY, true).apply();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent perfsIntent = new Intent(this, SettingsActivity.class);
            startActivityForResult(perfsIntent, PREFS_ACTIVITY);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void notifyTotal(int total) {
        String msg = getString(R.string.num_earthquake, total);

        Toast t = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        t.show();
    }
}
