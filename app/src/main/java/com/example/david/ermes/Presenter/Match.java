package com.example.david.ermes.Presenter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 30/05/2017.
 * Classe Match per creare una struttura per gli eventi sportivi
 */

public class Match {

    // DA CAMBIARE IN TIPO Date O long
    private String date;

    //da cambiare nel tipo Sport
    private String sport;
    private int imageID;


    public Match(String sport, String date,int imageID){
        this.date = date;
        this.sport = sport;
        this.imageID = imageID;

    }

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
    public static Match create(String sport, String date,int imageID) {
        return new Match(sport, date,imageID);
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

        match.add(create("Basket","Domani",basketballimage));
        match.add(create("Volley","25 dicembre",volleyimage));
        match.add(create("Calcio","Martedì",soccerimage));
        match.add(create("Tennis","Mercoledì",tennisimage));
        match.add(create("Ping pong","Lol never",pingpongimage));
        match.add(create("Golf","Ma come ci speri",golfballimage));
        match.add(create("Baseball","Ma come ci speri",baseballimage));
        match.add(create("Baseball","Ma come ci speri",baseballimage2));
        match.add(create("Baseball","Ma come ci speri",baseballimage3));

        return match;
    }
}
