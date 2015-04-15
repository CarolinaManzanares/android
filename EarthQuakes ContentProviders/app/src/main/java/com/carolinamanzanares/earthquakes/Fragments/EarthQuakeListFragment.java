package com.carolinamanzanares.earthquakes.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.ListFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.carolinamanzanares.earthquakes.DetailActivity;
import com.carolinamanzanares.earthquakes.Model.EarthQuakes;
import com.carolinamanzanares.earthquakes.R;

import com.carolinamanzanares.earthquakes.Services.DownloadEarthquakesService;
import com.carolinamanzanares.earthquakes.adapters.earthquakeAdapter;
import com.carolinamanzanares.earthquakes.providers.EarthQuakeDB;

import java.util.ArrayList;
import java.util.List;

/*
 * A fragment representing a list of EarthQuake.
 */
public class EarthQuakeListFragment extends ListFragment{

    private EarthQuakeDB earthQuakeDB;

    public static final String EARTHQUAKE_ITEM = "EARTHQUAKE_ITEM";

    private SharedPreferences prefs;
    private List<EarthQuakes> earthQuakes;
    private ArrayAdapter<EarthQuakes> aa;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        earthQuakes = new ArrayList<>();

        earthQuakeDB = new EarthQuakeDB(getActivity());

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //indicar que el fragmento va a tener un menu
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = super.onCreateView(inflater, container, savedInstanceState);

        aa = new earthquakeAdapter(getActivity(), R.layout.format_earthquake, earthQuakes);
        setListAdapter(aa);

        return layout;

    }

    @Override
    public void onResume() {
        super.onResume();

        int magnitude = Integer.parseInt(prefs.getString(getString(R.string.MAGNITUDE), "0"));

        earthQuakes.clear();
        earthQuakes.addAll(earthQuakeDB.getAllByMagnitude(magnitude));
        aa.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        EarthQuakes earthquake = earthQuakes.get(position);
        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);

        detailIntent.putExtra(EARTHQUAKE_ITEM, earthquake.getId());
        startActivity(detailIntent);
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
