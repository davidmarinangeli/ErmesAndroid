package com.example.david.ermes.Model.models;

import com.example.david.ermes.Model.db.DbModels;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by David on 21/07/2017.
 */

public class User {
    private static FirebaseUser currentUser;

    private String name;
    private String email;
    private String UID;
    private String city;
    private String idFavSport;
    private long birthDate;

    public User() {

    }

    public User(String name, String email, String UID, String city, String idFavSport,
                long birthDate) {
        this.name = name;
        this.email = email;
        this.UID = UID;
        this.city = city;
        this.idFavSport = idFavSport;
        this.birthDate = birthDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUID() {
        return this.UID;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public String getIdFavSport() { return this.idFavSport; }

    public void setIdFavSport(String idFavSport) { this.idFavSport = idFavSport; }

    public long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    public void save() {
        UserRepository.getInstance().saveUser(this);
    }

    public DbModels._User convertTo_User() {
        return new DbModels._User(
                this.name,
                this.email,
                this.idFavSport,
                this.city,
                this.birthDate
        );
    }

    public static String getCurrentUserId() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }

        return null;
    }
}
