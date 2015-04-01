package com.carolinamanzanares.earthquakes.Model;

/**
 * Created by cursomovil on 25/03/15.
 */
public class Coordinate {

    private double latitude;
    private double longitude;
    private double deth;

    public Coordinate(double latitude, double longitude, double deth) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.deth = deth;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getDeth() {
        return deth;
    }

    public void setDeth(double deth) {
        this.deth = deth;
    }
}
