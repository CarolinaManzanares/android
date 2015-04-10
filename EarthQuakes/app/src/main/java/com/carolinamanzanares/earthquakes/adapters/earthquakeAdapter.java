package com.carolinamanzanares.earthquakes.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carolinamanzanares.earthquakes.Model.EarthQuakes;
import com.carolinamanzanares.earthquakes.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by cursomovil on 25/03/15.
 */
public class earthquakeAdapter extends ArrayAdapter<EarthQuakes> {

    private int resource;

    public earthquakeAdapter(Context context, int resource, List<EarthQuakes> objects) {
        super(context, resource, objects);

        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Log.d("EARTHQUAKE", "en getView[position: " + position + "]");
        LinearLayout layout = (LinearLayout) convertView;

        if (convertView == null){
            //Si no existe la vista, la creamos
            layout = new LinearLayout(getContext());

            LayoutInflater li;
            String inflater = Context.LAYOUT_INFLATER_SERVICE;

            li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, layout, true);
        } else {
            layout = (LinearLayout) convertView;
        }

        EarthQuakes item = getItem(position);

        TextView magView = (TextView) layout.findViewById(R.id.magView);
        TextView placeView = (TextView) layout.findViewById(R.id.placeView);
        TextView timeView = (TextView) layout.findViewById(R.id.timeView);

        SimpleDateFormat sdt = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        magView.setText(String.valueOf(item.getMagnitude()));
        placeView.setText(item.getPlace());
        timeView.setText(sdt.format(item.getTime()));

        int n = (int)(item.getMagnitude() * 10);
        int color = Color.rgb((255 * n) / 100, (255 * (100 - n)) / 100 , 0);
        magView.setBackgroundColor(color);


        //return super.getView(position, convertView, parent);
        return layout;
    }
}
