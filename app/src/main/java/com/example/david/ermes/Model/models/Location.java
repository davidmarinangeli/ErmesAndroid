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
    private User userCreator;
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

    public Location(String name, double lat, double lon, User creator) {
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;
        this.userCreator = creator;
    }

    public User getLocation_creator() {
        return this.userCreator;
    }

    public void setLocation_creator(User location_creator) {
        this.userCreator = location_creator;
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

    public String getIdUserCreator() {
        return idUserCreator;
    }

    public String getId() { return this.id; }

    public DbModels._Location convertTo_Location() {
        String id_user;
        if (this.idUserCreator != null && this.idUserCreator.length() > 0) {
            id_user = this.idUserCreator;
        } else if (this.userCreator != null) {
            id_user = this.userCreator.getUID();
        } else {
            return null;
        }

        return new DbModels._Location(
                this.name,
                this.latitude,
                this.longitude,
                id_user
        );
    }

    public void fetchUserCreator(final FirebaseCallback firebaseCallback) {
        if (this.idUserCreator != null && this.idUserCreator.length() > 0) {
            UserRepository.getInstance().fetchUserById(this.idUserCreator, new FirebaseCallback() {
                @Override
                public void callback(Object object) {
                    userCreator = null;
                    if (object != null) {
                        userCreator = (User) object;
                        idUserCreator = ((User) object).getUID();
                    }

                    firebaseCallback.callback(userCreator);
                }
            });
        } else {
            firebaseCallback.callback(null);
        }
    }

    public void save() {
        LocationRepository.getInstance().saveLocation(this);
    }
}
