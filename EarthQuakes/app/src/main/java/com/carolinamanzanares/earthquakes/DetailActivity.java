package com.carolinamanzanares.earthquakes;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.carolinamanzanares.earthquakes.Fragments.EarthQuakeListFragment;
import com.carolinamanzanares.earthquakes.Model.EarthQuakes;
import com.carolinamanzanares.earthquakes.database.EarthQuakeDB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class DetailActivity extends ActionBarActivity {
    private static final int PREFS_ACTIVITY = 1;

    private EarthQuakeDB earthQuakeDB;
    private ArrayList<EarthQuakes> earthQuakes;
    private EarthQuakes earthQuake;

    private TextView placeView;
    private TextView magView;
    private TextView timeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datail_earthquake);

        placeView = (TextView) findViewById(R.id.placeView);
        magView = (TextView) findViewById(R.id.magView);
        timeView = (TextView) findViewById(R.id.timeView);

//        Intent detailIntent = getIntent();
//        id = detailIntent.getStringExtra(EarthQuakeListFragment.EARTHQUAKE_ITEM);

        // Acceder a la BD
        earthQuakeDB = new EarthQuakeDB(this);


        //populateView();


    }




    @Override
    protected void onResume() {
        super.onResume();

        Intent detailIntent = getIntent();
        //int id = detailIntent.getIntExtra(EarthQuakeListFragment.EARTHQUAKE_ITEM, 0);
        String id = detailIntent.getStringExtra(EarthQuakeListFragment.EARTHQUAKE_ITEM);
        Log.d("EARTHQUAKE", "Id: " + id);

        earthQuake = new EarthQuakes();
        earthQuake = earthQuakeDB.getAllById(id);

        populateView();


    }

    private void populateView() {

        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        placeView.setText(earthQuake.getPlace());
        magView.setText(String.valueOf(earthQuake.getMagnitude()));
        timeView.setText(sdt.format(earthQuake.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_activity, menu);
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
}
