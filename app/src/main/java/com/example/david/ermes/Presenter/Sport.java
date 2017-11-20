package com.example.david.ermes.Presenter;

import com.example.david.ermes.Model.DatabaseManager;
import com.example.david.ermes.Model.Models;
import com.example.david.ermes.Presenter.FirebaseCallback;

import java.util.List;

/**
 * Created by nicol on 02/11/2017.
 */

public class Sport {
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

    public static void fetchAllSports(final FirebaseCallback fCallback) {
        (new DatabaseManager()).fetchAllSports(new FirebaseCallback() {
            @Override
            public void callback(List list) {
                fCallback.callback(Models._Sport.convertToSportList(list));
            }
        });
    }
}
