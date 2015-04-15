package com.carolinamanzanares.earthquakes.Model;

import android.content.pm.InstrumentationInfo;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by cursomovil on 25/03/15.
 */
public class EarthQuakes{

    private String _id;
    private Date time;
    private Coordinate coords;
    private double magnitude;
    private String place;
    private String URL;

    public EarthQuakes(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public EarthQuakes(String _id, Date time, Coordinate coords, double magnitude, String place) {
        this._id = _id;
        this.time = time;
        this.coords = coords;
        this.magnitude = magnitude;
        this.place = place;
    }

    public EarthQuakes() {

    }



    @Override
    public String toString() {
        return this.getPlace();
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = new Date(time);
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Coordinate getCoords() {
        return coords;
    }

    public void setCoords(Coordinate coords) {
        this.coords = coords;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

}
