package com.example.david.ermes.Model.models;

import com.example.david.ermes.Model.db.DbModels;
import com.example.david.ermes.Model.db.DatabaseManager;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.repository.LocationRepository;
import com.example.david.ermes.Model.repository.UserRepository;

import java.io.Serializable;

/**
 * Created by david on 11/14/2017.
 */

public class Location implements Serializable{
    private String idUserCreator;
    private double latitude;
    private double longitude;
    private String name;
    private String id;

    public Location() {}

    public Location(String name){
        this.name = name;
    }

    public Location(String id, String name, double lat, double lon, String idUserCreator) {
        this.id = id;
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;
        this.idUserCreator = idUserCreator;
    }

    public Location(String name, double lat, double lon, String idUserCreator) {
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;
        this.idUserCreator = idUserCreator;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdUserCreator() {
        return idUserCreator;
    }

    public void setIdUserCreator(String id) { this.idUserCreator = id; }

    public String getId() { return this.id; }

    public double getDistanceFromLocation(Location location) {
        double distance = 0.0;

        if (location != null) {
            distance = Math.sqrt(
                    Math.abs(this.getLatitude() - location.getLatitude()) +
                    Math.abs(this.getLongitude() - location.getLongitude())
            );
        }

        return distance;
    }

    public DbModels._Location convertTo_Location() {
        return new DbModels._Location(
                this.name,
                this.latitude,
                this.longitude,
                this.idUserCreator
        );
    }

    public void save() {
        LocationRepository.getInstance().saveLocation(this);
    }
}
