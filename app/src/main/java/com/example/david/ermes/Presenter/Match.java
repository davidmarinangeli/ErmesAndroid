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

    private DatabaseManager db;

    public Match(String idOwner, String location, Date date) {
        this.location = location;
        this.date = date;
        this.idOwner = idOwner;

        this.db = new DatabaseManager();
    }

    public void save() {
        Models._Match m = new Models._Match(this.idOwner, this.date, this.location);
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

    public static void fetchMatchesByIdOwner(String id) {
        DatabaseManager fetchDB = new DatabaseManager();
        //fetchDB.fetchMatchesByIdOwner(id);
    }

}
