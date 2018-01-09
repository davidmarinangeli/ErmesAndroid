package com.example.david.ermes.Presenter.utils;

import android.location.Location;

/**
 * Created by david on 1/9/2018.
 */

public class LocationUtils {

    public static com.example.david.ermes.Model.models.Location fromAndroidLocationtoErmesLocation(Location androidlocation){
        com.example.david.ermes.Model.models.Location ermeslocation = null;

        if (androidlocation != null) {
            ermeslocation = new com.example.david.ermes.Model.models.Location();
            ermeslocation.setLatitude(androidlocation.getLatitude());
            ermeslocation.setLongitude(androidlocation.getLongitude());
        }

        return ermeslocation;
    }
}
