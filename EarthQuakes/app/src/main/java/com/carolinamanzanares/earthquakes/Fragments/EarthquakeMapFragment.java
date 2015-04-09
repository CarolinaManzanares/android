package com.carolinamanzanares.earthquakes.Fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carolinamanzanares.earthquakes.Model.EarthQuakes;
import com.carolinamanzanares.earthquakes.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarthquakeMapFragment extends MapFragment implements GoogleMap.OnMapLoadedCallback {

    private GoogleMap map;
    private List<EarthQuakes> earthQuakes;


    public EarthquakeMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        map = getMap();
        map.setOnMapLoadedCallback(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setEarthQuake(List<EarthQuakes> earthQuakes){
        this.earthQuakes = earthQuakes;

    }


    @Override
    public void onMapLoaded() {

        EarthQuakes earthquake;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < earthQuakes.size(); i++){
            earthquake = earthQuakes.get(i);
            LatLng position = new LatLng(earthquake.getCoords().getLatitude(), earthquake.getCoords().getLongitude());
            String title = earthquake.getPlace();
            String snippet = position.toString();

            map.addMarker(new MarkerOptions().position(position).title(title).snippet(snippet));
            builder.include(position);
        }

        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        //map.moveCamera(cu);

        map.animateCamera(cu);



    }
}
