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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EarthQuakeMapDetailFragment extends AbstractMapFragment {

    private List<EarthQuakes> earthQuakes;

    private EarthQuakes earthquake;
    private CameraUpdate camUpd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);

        //earthquake = new EarthQuakes();


        //getData();


        return layout;
    }

    @Override
    protected void getData() {

        Intent intent = getActivity().getIntent();
        String id = intent.getStringExtra(EarthQuakeListFragment.EARTHQUAKE_ITEM);
        earthquake = earthquakeDB.getAllById(id);
    }

    @Override
    protected void paintMap() {

        LatLng position;
        String title;
        String snippet;

        position = new LatLng(earthquake.getCoords().getLatitude(), earthquake.getCoords().getLongitude());
        title = earthquake.getPlace();
        snippet = "Mag: " + String.valueOf(earthquake.getMagnitude()) + ". " + position.toString();

        map.addMarker(new MarkerOptions().position(position).title(title).snippet(snippet));

        CameraPosition camPos =	new	CameraPosition.Builder().target(position)
                .zoom(4)
                .bearing(0)
                .tilt(90)
                .build();

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String url = earthquake.getURL();
                Intent urlIntent = new Intent(Intent.ACTION_VIEW);
                urlIntent.setData(Uri.parse(url));
                startActivity(urlIntent);
            }
        });

        camUpd	= CameraUpdateFactory.newCameraPosition(camPos);
        map.animateCamera(camUpd);


    }



}
