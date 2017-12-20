package com.example.david.ermes.Model.repository;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.db.LocationDatabaseRepository;
import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.db.DbModels._Location;

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
        LocationDatabaseRepository.getInstance().fetchLocationById(id, new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                Location location = null;
                if (object != null) {
                    location = ((_Location) object).convertToLocation();
                }

                firebaseCallback.callback(location);
            }
        });
    }
}
