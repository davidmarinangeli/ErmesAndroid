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
    private Uri photoURL;
    private String UID;

    private DatabaseManager db;

    public User() {
        this.db = new DatabaseManager();
    }

    public User(String name, String email, Uri photoURL, String UID) {
        this.name = name;
        this.email = email;
        this.photoURL = photoURL;
        this.UID = UID;

        db = new DatabaseManager();
    }

    public static User getCurrentUser() {
        DatabaseManager db = new DatabaseManager();
        return db.getCurrentUser();
    }

    public void save() {
        Models._User u = new Models._User(this.name);
        this.db.saveUser(this.UID, u);
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public Uri getPhotoURL() {
        return this.photoURL;
    }

    public String getUID() {
        return this.UID;
    }
}
