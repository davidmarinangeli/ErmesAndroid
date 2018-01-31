package com.example.david.ermes.Model.db;

import android.util.Log;

import com.example.david.ermes.Model.db.DbModels._Sport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 04/12/2017.
 */

public class SportDatabaseRepository {
    private static SportDatabaseRepository instance = new SportDatabaseRepository();

    public static SportDatabaseRepository getInstance() {
        return instance;
    }

    private DatabaseReference ref;

    public SportDatabaseRepository() {
        this.ref = DatabaseManager.get().getSportsRef();
    }

    public void fetchSportById(final String id, final FirebaseCallback firebaseCallback) {
        if (id != null && id.length() > 0) {
            Query query = this.ref.child(id);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    _Sport sport = dataSnapshot.getValue(_Sport.class);
                    sport.setID(id);

                    firebaseCallback.callback(sport);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (firebaseCallback != null) {
                        firebaseCallback.callback(null);
                    }
                }
            });
        } else {
            firebaseCallback.callback(null);
        }
    }

    public void fetchSportByName(final String name, final FirebaseCallback firebaseCallback) {
        if (name != null && name.length() > 0) {

            Query query = this.ref.orderByChild("name").equalTo(name);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        _Sport sport = d.getValue(_Sport.class);

                        sport.setID(d.getKey());
                        firebaseCallback.callback(sport);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (firebaseCallback != null) {
                        firebaseCallback.callback(null);
                    }
                }
            });
        } else {
            firebaseCallback.callback(null);
        }
    }

    public void fetchSportByName(final String name, final FirebaseCallback firebaseCallback) {
        if (name != null && name.length() > 0) {

            Query query = this.ref.orderByChild("name").equalTo(name);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        _Sport sport = d.getValue(_Sport.class);

                        sport.setID(d.getKey());
                        firebaseCallback.callback(sport);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            firebaseCallback.callback(null);
        }
    }

    public void fetchAllSports(final FirebaseCallback fc) {
        final List<_Sport> list = new ArrayList<>();
        this.ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // DbModels._User value = dataSnapshot.getValue(DbModels._User.class);
                list.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Log.d("DATASNAPSHOT", d.toString());
                    _Sport sport = d.getValue(_Sport.class);
                    sport.setID(d.getKey());

                    list.add(sport);
//                    list.get(list.size() - 1).setID(d.getKey().toString());
                }
                fc.callback(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                if (fc != null) {
                    fc.callback(null);
                }
            }
        });
    }
}
