package com.example.david.ermes.Presenter;

import com.example.david.ermes.Model.DatabaseManager;
import com.example.david.ermes.Model.Models;

import java.io.Serializable;

/**
 * Created by david on 11/14/2017.
 */

public class Location implements Serializable{
    private User location_creator;
    private double latitude;
    private double longitude;
    private String name;

    private DatabaseManager db;

    public Location() {
        this.db = new DatabaseManager();
    }

    public Location(String name){
        this.name = name;
        this.db = new DatabaseManager();
    }

    public Location(String name, double lat, double lon, User creator) {
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;
        this.location_creator = creator;
        this.db = new DatabaseManager();
    }

    public User getLocation_creator() {
        return location_creator;
    }

    public void setLocation_creator(User location_creator) {
        this.location_creator = location_creator;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save() {
        Models._Location location = new Models._Location(
                this.name,
                this.latitude,
                this.longitude,
                this.location_creator.getUID()
        );

        this.db.saveLocation(location);
    }
}
