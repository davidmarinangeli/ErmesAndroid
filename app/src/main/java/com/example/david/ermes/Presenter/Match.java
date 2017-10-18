package com.example.david.ermes.Presenter;

import android.content.Context;

import com.example.david.ermes.Model.DatabaseManager;
import com.example.david.ermes.Model.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by David on 30/05/2017.
 * Classe Match per creare una struttura per gli eventi sportivi
 */

public class Match implements Serializable{

    // DA CAMBIARE IN TIPO Date O long

    private long data;

    //da cambiare nel tipo Sport
    private String sport;
    private int imageID;
    private String place;
    private Date date;

    private DatabaseManager db;

    public Match(String sport, long  data, int imageID, String place, Date date){
        this.data = data;
        this.sport = sport;
        this.imageID = imageID;
        this.place = place;
        this.date = date;

        this.db = new DatabaseManager();
    }

    public void save() {
        Models._Match m = new Models._Match(this.date, this.place);
        this.db.saveMatch(m);
    }

    public static List<Match> searchMatchesByIdOwner(String id) {
        DatabaseManager db = new DatabaseManager();
        db.fetchMatchesByIdOwner(id);
        return null;
    }

    public String getPlace() { return place; }

    public void setPlace(String place) { this.place = place; }

    public long getDate() {
        return data;
    }

    public void setDate(long date) {
        this.data = date;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }



    //metodo che crea un nuovo oggetto Match SI LO SO CHE E' NA MERDA E NON DOVREBBE STARCI, E' "TEMPORANEO"
    public static Match create(String sport, long date,int imageID, String place) {
        return new Match(sport, date,imageID,place, null);
    }

    // aggiungere altra roba di un match qui sotto


    // lista di prova che genera eventi
    public static List<Match> toyListofMatches(Context cx){
        List<Match> match = new ArrayList<>();

        int basketballimage = cx.getResources().getIdentifier("basketball96", "drawable", cx.getPackageName());
        int soccerimage = cx.getResources().getIdentifier("soccerball96", "drawable", cx.getPackageName());
        int volleyimage = cx.getResources().getIdentifier("volleyball696", "drawable", cx.getPackageName());
        int tennisimage = cx.getResources().getIdentifier("tennisracquet96", "drawable", cx.getPackageName());
        int pingpongimage = cx.getResources().getIdentifier("pingpong96", "drawable", cx.getPackageName());
        int golfballimage = cx.getResources().getIdentifier("golfball96", "drawable", cx.getPackageName());
        int baseballimage = cx.getResources().getIdentifier("baseball96", "drawable", cx.getPackageName());
        int baseballimage2 = cx.getResources().getIdentifier("baseball96", "drawable", cx.getPackageName());
        int baseballimage3 = cx.getResources().getIdentifier("baseball96", "drawable", cx.getPackageName());

        match.add(create("Basket",1500547551959L,basketballimage,"Parco le albere"));
        /*
        match.add(create("Volley",1500495940958L,volleyimage,"Via Alessandro Volta"));
        match.add(create("Calcio",1500547551959L,soccerimage,"Via Rosmini"));
        match.add(create("Tennis",1498694403500L,tennisimage,"Via Battisti"));
        match.add(create("Ping pong",969695100000L,pingpongimage,"Via Roma"));
        match.add(create("Golf",1500495941958L,golfballimage,"Via bubu"));
        match.add(create("Baseball",1500547551959L,baseballimage,"Via non ho più nomi"));
        match.add(create("Baseball",1499126440030L,baseballimage2,"Via pls"));
        match.add(create("Baseball",969695100000L,baseballimage3, "Via sthap"));



         */



        return match;
    }

}
