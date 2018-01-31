package com.example.david.ermes.Model.repository;

import com.example.david.ermes.Model.db.DbModels;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.db.LocationDatabaseRepository;
import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.db.DbModels._Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 04/12/2017.
 */

public class LocationRepository {
    private static final LocationRepository instance = new LocationRepository();

    public static LocationRepository getInstance() {
        return instance;
    }

    private LocationRepository() {

    }

    public void saveLocation(Location location) {
        if (location != null) {
            LocationDatabaseRepository.getInstance().push(location.convertTo_Location());
        }
    }

    public void fetchLocationById(String id, final FirebaseCallback firebaseCallback) {
        LocationDatabaseRepository.getInstance().fetchLocationById(id, object -> {
            Location location = null;
            if (object != null) {
                location = ((_Location) object).convertToLocation();
            }

            firebaseCallback.callback(location);
        });
    }


    public void fetchAllLocations(final FirebaseCallback firebaseCallback){
        LocationDatabaseRepository.getInstance().fetchAllLocations(object -> {
            if (object != null){
                firebaseCallback.callback(_Location.convertToLocationList(
                        (List<_Location>) object
                ));
            } else {
                firebaseCallback.callback(null);
            }
        });
    }


    public void fetchLocationsByRange(Location location, double range, final FirebaseCallback firebaseCallback) {
        LocationDatabaseRepository.getInstance().fetchLocationsByRange(
                location.getLatitude(),
                location.getLongitude(),
                range,
                object -> {
                    if (object != null) {
                        firebaseCallback.callback(_Location.convertToLocationList(
                                (List<_Location>) object
                        ));
                    }
                }
        );
    }

    public void fetchLocationsByProximity(Location location, FirebaseCallback firebaseCallback) {
        LocationDatabaseRepository.getInstance().fetchLocationsByProximity(location, firebaseCallback);
    }
}
