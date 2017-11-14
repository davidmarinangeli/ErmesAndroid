package com.example.david.ermes.Presenter;

import android.net.Uri;

import com.example.david.ermes.Model.DatabaseManager;
import com.example.david.ermes.Model.Models;

/**
 * Created by David on 21/07/2017.
 */

public class User {
    private String name;
    private String email;
    private String UID;
    private String city;
    private String idFavSport;

    private DatabaseManager db;

    public User() {
        this.db = new DatabaseManager();
    }

    public User(String name, String email, String UID, String city, String idFavSport) {
        this.name = name;
        this.email = email;
        this.UID = UID;
        this.city = city;
        this.idFavSport = idFavSport;

        db = new DatabaseManager();
    }

    public static User getCurrentUser() {
        DatabaseManager db = new DatabaseManager();
        return db.getCurrentUser();
    }

    public void save() {
        Models._User u = new Models._User(this.idFavSport, this.city);
        this.db.saveUser(this.UID, u);
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

    public String getIdFavSport() { return this.idFavSport; }

    public String getCity() { return this.city; }
}
