package com.example.david.ermes.Model.models;

/**
 * Created by David on 21/07/2017.
 */

public class User {
    private String name;
    private String email;
    private String UID;
    private String city;
    private String idFavSport;

    public User() {

    }

    public User(String name, String email, String UID, String city, String idFavSport) {
        this.name = name;
        this.email = email;
        this.UID = UID;
        this.city = city;
        this.idFavSport = idFavSport;

    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUID() {
        return this.UID;
    }

    public String getIdFavSport() {
        return this.idFavSport;
    }

    public String getCity() {
        return this.city;
    }
}
