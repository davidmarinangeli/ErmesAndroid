package com.example.david.ermes.Presenter.utils;

import android.content.Context;

/**
 * Created by david on 10/24/2017.
 */

public class FetchMatchUtils {

    public FetchMatchUtils() {
    }
    public static int setImageToMatch(Context cx,String sport){
        int image_tag = 0;
        switch (sport) {
            case "Basketball":
                image_tag = cx.getResources().getIdentifier("basketball96", "drawable", cx.getPackageName());
                break;
            case "Volleyball":
                image_tag = cx.getResources().getIdentifier("volleyball696", "drawable", cx.getPackageName());
                break;
            case "Tennis":
                image_tag = cx.getResources().getIdentifier("tennisracquet96", "drawable", cx.getPackageName());
                break;
            case "Football":
                image_tag = cx.getResources().getIdentifier("soccerball96", "drawable", cx.getPackageName());
                break;
            case "Ping Pong":
                image_tag = cx.getResources().getIdentifier("pingpong96", "drawable", cx.getPackageName());
                break;
        }
        return image_tag;
    }
}
