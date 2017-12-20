package com.example.david.ermes.Model.db;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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

    public void fetchLocationById(String id, final FirebaseCallback firebaseCallback) {
        this.ref.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DbModels._Location location = dataSnapshot.getValue(DbModels._Location.class);
                location.setId(dataSnapshot.getKey());

                firebaseCallback.callback(location);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                firebaseCallback.callback(null);
            }
        });
    }
}
