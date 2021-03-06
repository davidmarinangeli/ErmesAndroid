package com.example.david.ermes.Model.db;

import android.util.Log;

import com.example.david.ermes.Model.models.Location;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nicol on 04/12/2017.
 */

public class LocationDatabaseRepository {
    private static LocationDatabaseRepository instance = new LocationDatabaseRepository();

    public static LocationDatabaseRepository getInstance() {
        return instance;
    }

    private DatabaseReference ref;

    public LocationDatabaseRepository() {
        this.ref = DatabaseManager.get().getLocationsRef();
    }

    public void push(DbModels._Location location) {
        if (location.getID() != null && !location.getID().isEmpty()) {
            this.ref.child(location.getID()).setValue(location);
        } else {
            this.ref.push().setValue(location);
        }
    }

    public void fetchAllLocations(final FirebaseCallback firebaseCallback) {
        this.ref.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<DbModels._Location> list = new ArrayList<>();

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    DbModels._Location loc = d.getValue(DbModels._Location.class);
                    loc.setID(d.getKey());
                    list.add(loc);
                }

                firebaseCallback.callback(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (firebaseCallback != null) {
                    firebaseCallback.callback(null);
                }
            }
        });
    }

    public void fetchLocationById(String id, final FirebaseCallback firebaseCallback) {
        this.ref.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DbModels._Location location = dataSnapshot.getValue(DbModels._Location.class);
                location.setID(dataSnapshot.getKey());

                firebaseCallback.callback(location);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (firebaseCallback != null) {
                    firebaseCallback.callback(null);
                }
            }
        });
    }

    public void fetchLocationsByRange(double center_x, final double center_y, final double range,
                                      final FirebaseCallback firebaseCallback) {
        this.ref.orderByChild("x").startAt(center_x - range).endAt(center_x + range)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<DbModels._Location> list = new ArrayList<>();

                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            DbModels._Location loc = d.getValue(DbModels._Location.class);

                            if (loc.y >= center_y - range && loc.y <= center_y + range) {
                                list.add(loc);
                            }
                        }

                        firebaseCallback.callback(list);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (firebaseCallback != null) {
                            firebaseCallback.callback(null);
                        }
                    }
                });
    }

    public void fetchLocationsByProximity(final Location location,
                                          final FirebaseCallback firebaseCallback) {
        fetchAllLocations(object -> {
            if (object != null) {
                List<Location> list = DbModels._Location.convertToLocationList(
                        (List<DbModels._Location>) object);

                Collections.sort(list, (l1, l2) -> {
                    double d1 = l1.getDistanceFromLocation(location);
                    double d2 = l2.getDistanceFromLocation(location);

                    return d1 > d2 ? 1
                            : d1 < d2 ? -1
                            : 0;
                });

                firebaseCallback.callback(list);
            } else {
                firebaseCallback.callback(null);
            }
        });
    }
}
