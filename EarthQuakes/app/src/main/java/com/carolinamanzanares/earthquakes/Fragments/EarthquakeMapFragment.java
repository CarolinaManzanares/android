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
    private EarthQuakes earthQuake;


    public EarthquakeMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = super.onCreateView(inflater, container, savedInstanceState);

        map = getMap();
        map.setOnMapLoadedCallback(this);

        return layout;
    }

    public void setEarthQuake(List<EarthQuakes> earthQuakes){
        this.earthQuakes = earthQuakes;

    }


    @Override
    public void onMapLoaded() {
        LatLng position;
        String title;
        String snippet;
        CameraUpdate camUpd;

        if (earthQuakes.size() == 1){
            earthQuake = earthQuakes.get(0);
            position = new LatLng(earthQuake.getCoords().getLatitude(), earthQuake.getCoords().getLongitude());
            title = earthQuake.getPlace();
            snippet = "Mag: " + String.valueOf(earthQuake.getMagnitude()) + ". " + position.toString();

            map.addMarker(new MarkerOptions().position(position).title(title).snippet(snippet));

            CameraPosition camPos =	new	CameraPosition.Builder().target(position)
                    .zoom(4)
                    .bearing(0)
                    .tilt(90)
                    .build();

            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    String url = earthQuake.getURL();
                    Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                    urlIntent.setData(Uri.parse(url));
                    startActivity(urlIntent);
                }
            });

            camUpd	= CameraUpdateFactory.newCameraPosition(camPos);

            map.animateCamera(camUpd);
        }
        else {
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


        }

        map.animateCamera(camUpd);



    }
}
