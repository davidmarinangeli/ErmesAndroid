package com.example.david.ermes.Presenter;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 30/05/2017.
 * Classe Match per creare una struttura per gli eventi sportivi
 */

public class Match implements Serializable{

    // DA CAMBIARE IN TIPO Date O long

    // Attenzione (da fare): quando definisco un match INIZIALIZZO LA DATE sia per il giorno che per l'orario.
    private String date;

    //da cambiare nel tipo Sport
    private String sport;
    private int imageID;
    private String place;


    public Match(String sport, String date,int imageID, String place){
        this.date = date;
        this.sport = sport;
        this.imageID = imageID;
        this.place = place;

    }

    public String getPlace() { return place; }

    public void setPlace(String place) { this.place = place; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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


    //metodo che crea un nuovo oggetto Match
    public static Match create(String sport, String date,int imageID, String place) {
        return new Match(sport, date,imageID,place);
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

        match.add(create("Basket","11 novembre",basketballimage,"Parco le albere"));
        match.add(create("Volley","25 dicembre",volleyimage,"Via Alessandro Volta"));
        match.add(create("Calcio","11 settembre",soccerimage,"Via Rosmini"));
        match.add(create("Tennis","16 giugno",tennisimage,"Via Battisti"));
        match.add(create("Ping pong","3 maggio",pingpongimage,"Via Roma"));
        match.add(create("Golf","Ma come ci speri",golfballimage,"Via bubu"));
        match.add(create("Baseball","Ma come ci speri",baseballimage,"Via non ho più nomi"));
        match.add(create("Baseball","Ma come ci speri",baseballimage2,"Via pls"));
        match.add(create("Baseball","Ma come ci speri",baseballimage3, "Via sthap"));

        return match;
    }
}
