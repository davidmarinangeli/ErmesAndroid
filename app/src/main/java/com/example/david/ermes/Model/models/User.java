package com.example.david.ermes.Model.models;

import com.example.david.ermes.Model.db.DbModels;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Model.repository.UserRepository;

/**
 * Created by David on 21/07/2017.
 */

public class User {
    private String name;
    private String email;
    private String UID;
    private String city;
    private Sport favSport;
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

    public void setName(String name) {
        this.name = name;
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

    public Sport getFavSport() {
        return favSport;
    }

    public void setFavSport(Sport favSport) {
        this.favSport = favSport;
        this.idFavSport = favSport.getID();
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public void save() {
        UserRepository.getInstance().saveUser(this);
    }

    public DbModels._User convertTo_User() {
        return new DbModels._User(
                this.name,
                this.email,
                this.idFavSport,
                this.city
        );
    }

    public void fetchFavoriteSport(final FirebaseCallback firebaseCallback) {
        SportRepository.getInstance().fetchSportById(this.idFavSport, new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                if (object != null) {
                    favSport = (Sport) object;
                }

                firebaseCallback.callback(favSport);
            }
        });
    }
}
