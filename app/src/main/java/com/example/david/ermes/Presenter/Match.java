package com.example.david.ermes.Presenter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.example.david.ermes.Model.DatabaseManager;
import com.example.david.ermes.Model.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by David on 30/05/2017.
 * Classe Match per creare una struttura per gli eventi sportivi
 */

public class Match implements Serializable {
    private String location;
    private Date date;
    private String idOwner;
    private boolean isPublic;
    private String idSport;
    private int maxPlayers;
    private List<User> pending;
    private List<User> partecipants;
    private int numGuests;
    private List<String> missingStuff;

    private DatabaseManager db;

    public Match() {}

    public Match(String idOwner, String location, Date date, boolean isPublic,
                 String idSport, int maxPlayers, int numGuests, List<String> missingStuff) {
        this.location = location;
        this.date = date;
        this.idOwner = idOwner;
        this.isPublic = isPublic;
        this.idSport = idSport;
        this.maxPlayers = maxPlayers;
        this.numGuests = numGuests;
        this.missingStuff = missingStuff;

        this.db = new DatabaseManager();
    }

    public void save() {
        Models._Match m = new Models._Match(this.idOwner, this.date, this.location, this.isPublic,
                this.idSport, this.maxPlayers, this.numGuests, this.missingStuff);
        this.db.saveMatch(m);
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public static void fetchMatchesByIdOwner(String id, final FirebaseCallback fCallback) {
        (new DatabaseManager()).fetchMatchesByIdOwner(id, new FirebaseCallback() {
            @Override
            public void callback(List list) {
                fCallback.callback(Models._Match.convertToMatchList(list));
            }
        });
    }

}
