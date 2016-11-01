package com.example.asus.mapstations;

/**
 * Created by Asus on 29/10/2016.
 */

public class Station {
    private String place;
    private double lat;
    private double lon;
    private String information;
    public Station() {

    }
    public Station(String place, double lat, double lon,String information) {
        this.place = place;
        this.lat = lat;
        this.lon = lon;
        this.information=information;
    }
    public Station(double lat, double lon,String information) {
        this.lat = lat;
        this.lon = lon;
        this.information=information;
    }
    public Station(String place,double lat, double lon) {
        this.place=place;
        this.lat = lat;
        this.lon = lon;
    }
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
