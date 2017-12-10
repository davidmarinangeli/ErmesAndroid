package com.example.david.ermes.Model.models;

import com.example.david.ermes.Model.db.DbModels;
import com.example.david.ermes.Model.db.DatabaseManager;
import com.example.david.ermes.Model.db.FirebaseCallback;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nicol on 02/11/2017.
 */

public class Sport implements Serializable {
    private String name;
    private int numPlayers;
    private String id;

    public Sport() {}

    public Sport(String id, String name, int numPlayers) {
        this.name = name;
        this.numPlayers = numPlayers;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getNumPlayers() {
        return this.numPlayers;
    }

    public String getID() {
        return this.id;
    }
}
