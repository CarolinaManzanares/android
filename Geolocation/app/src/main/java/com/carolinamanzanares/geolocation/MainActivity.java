package com.carolinamanzanares.geolocation;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements com.carolinamanzanares.geolocation.Listeners.LocationListener.AddLocationInterface{

    private TextView lblLatitude;
    private TextView lblLongitude;
    private TextView lblAltitude;
    private TextView lblSpeed;

    private String provider;
    private LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblLatitude = (TextView) findViewById(R.id.lblLatitude);
        lblLongitude = (TextView) findViewById(R.id.lblLongitude);
        lblAltitude = (TextView) findViewById(R.id.lblAltitude);
        lblSpeed = (TextView) findViewById(R.id.lblVelocidad);

        getLocationProvider();

        ListenLocationChanges();

    }

    private void ListenLocationChanges() {

        int t = 5000;
        int distance = 5;

        LocationListener listener = new LocationListener(this);

        locationManager.requestLocationUpdates(provider, t, distance, listener);


    }

    private void getLocationProvider() {

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedRequired(true);
        criteria.setAltitudeRequired(true);

        provider = locationManager.getBestProvider(criteria, true);

        Log.d("GEOLOCATION", provider);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addLocation(Location location) {
        lblLatitude.setText((String.valueOf(location.getLatitude())));
        lblLongitude.setText((String.valueOf(location.getLongitude())));
        lblAltitude.setText((String.valueOf(location.getAltitude())));
        lblSpeed.setText((String.valueOf(location.getSpeed())));
    }
}
