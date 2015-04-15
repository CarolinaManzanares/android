package com.carolinamanzanares.earthquakes.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carolinamanzanares.earthquakes.Model.EarthQuakes;
import com.carolinamanzanares.earthquakes.R;
import com.carolinamanzanares.earthquakes.Services.DownloadEarthquakesService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarthquakeMapFragment extends AbstractMapFragment {

    private List<EarthQuakes> earthQuakes;
    private EarthQuakes earthQuake;
    private SharedPreferences prefs;
    private CameraUpdate camUpd;


    public EarthquakeMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = super.onCreateView(inflater, container, savedInstanceState);

        earthQuakes = new ArrayList<>();

        return layout;
    }

    @Override
    protected void getData() {
        int magnitude = Integer.parseInt(prefs.getString(getString(R.string.MAGNITUDE), "0"));

        earthQuakes.clear();
        earthQuakes.addAll(earthquakeDB.getAllByMagnitude(magnitude));

    }

    @Override
    protected void paintMap() {

        LatLng position;
        String title;
        String snippet;
        //CameraUpdate camUpd;

        map.clear();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < earthQuakes.size(); i++){
            earthQuake = earthQuakes.get(i);
            position = new LatLng(earthQuake.getCoords().getLatitude(), earthQuake.getCoords().getLongitude());
            title = earthQuake.getPlace();
            snippet = position.toString();

            map.addMarker(new MarkerOptions().position(position).title(title).snippet(snippet));
            builder.include(position);
        }

        LatLngBounds bounds = builder.build();
        camUpd = CameraUpdateFactory.newLatLngBounds(bounds, 0);

        map.animateCamera(camUpd);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh){
            //lanzar el servicio
            Intent intent = new Intent(getActivity(), DownloadEarthquakesService.class);
            getActivity().startService(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
