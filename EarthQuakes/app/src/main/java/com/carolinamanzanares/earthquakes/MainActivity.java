package com.carolinamanzanares.earthquakes;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Tabs
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab = actionBar.newTab()
                .setText(R.string.List)
                .setTabListener((ActionBar.TabListener) new TabListener<EarthQuakeListFragment>(
                        this, R.id.main_frame, EarthQuakeListFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.Map)
                .setTabListener((ActionBar.TabListener) new TabListener<EarthquakeMapFragment>(
                        this, R.id.main_frame, EarthquakeMapFragment.class));
        actionBar.addTab(tab);



        //downloadEarthQuakes(); //cuando usamos task y service
        checkToSetAlarm();  //cuando usamos alarmas
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
        Log.d("EARTHQUAKES", KEY);

    }

    private void downloadEarthQuakes() {
//        DownloadEarthquakesTask task = new DownloadEarthquakesTask(this, this);
//        task.execute(getString(R.string.earthquakes_url));

//        Intent download = new Intent(this, DownloadEarthquakesService.class);
//        startService(download);



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
