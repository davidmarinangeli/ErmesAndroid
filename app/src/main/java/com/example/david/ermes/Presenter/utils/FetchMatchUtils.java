package com.example.david.ermes.Presenter.utils;

import android.content.Context;

/**
 * Created by david on 10/24/2017.
 */

public class FetchMatchUtils {

    public FetchMatchUtils() {
    }

    public void setImageToMatch(Context cx,String sport){
        int image_tag;
        switch (sport) {
            case "Basket":
                image_tag = cx.getResources().getIdentifier("basketball96", "drawable", cx.getPackageName());
                break;
            case "Volley":
                image_tag = cx.getResources().getIdentifier("volleyball696", "drawable", cx.getPackageName());
                break;
            case "Tennis":
                image_tag = cx.getResources().getIdentifier("tennisracquet96", "drawable", cx.getPackageName());
                break;
            case "Calcio":
                image_tag = cx.getResources().getIdentifier("soccerball96", "drawable", cx.getPackageName());
                break;
            case "Ping pong":
                image_tag = cx.getResources().getIdentifier("pingpong96", "drawable", cx.getPackageName());
                break;
            case "Golf":
                image_tag = cx.getResources().getIdentifier("golfball96", "drawable", cx.getPackageName());
                break;
        }
    }
}
