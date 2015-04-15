package com.carolinamanzanares.earthquakes.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carolinamanzanares.earthquakes.providers.EarthQuakeDB;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class AbstractMapFragment extends MapFragment implements GoogleMap.OnMapLoadedCallback{

    protected GoogleMap map;
    protected EarthQuakeDB earthquakeDB;


    public AbstractMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        earthquakeDB = new EarthQuakeDB(getActivity());

        //indicar que el fragmento va a tener un menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);

        map = getMap();
        map.setOnMapLoadedCallback(this);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        setupMapIfINeeded();
    }

    protected void setupMapIfINeeded(){

        if(map == null){
            map = getMap();
        }
    }

    //    protected MarkerOptions CreateMarker(EarthQuakes earthQuake){
//
//        return marker;
//
//    }

    @Override
    public void onMapLoaded() {
        getData();
        paintMap();
    }

    protected abstract void getData();

    protected abstract void paintMap();

}
