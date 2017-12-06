package com.example.david.ermes.Model.db;

import com.google.firebase.database.DatabaseReference;

/**
 * Created by nicol on 04/12/2017.
 */

public class LocationDatabaseRepository {
    private static LocationDatabaseRepository instance = new LocationDatabaseRepository();

    public static LocationDatabaseRepository getInstance() { return instance; }

    private DatabaseReference ref;

    public LocationDatabaseRepository() {
        this.ref = DatabaseManager.get().getLocationsRef();
    }

    public void push(DbModels._Location location) {
        this.ref.push().setValue(location);
    }
}
