package com.example.david.ermes.Model.repository;

import com.example.david.ermes.Model.db.DbModels;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.db.LocationDatabaseRepository;
import com.example.david.ermes.Model.db.MatchesDatabaseRepository;
import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.models.User;

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
}
