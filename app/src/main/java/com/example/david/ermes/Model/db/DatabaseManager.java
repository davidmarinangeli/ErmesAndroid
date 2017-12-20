package com.example.david.ermes.Model.db;

import android.util.Log;

import com.example.david.ermes.Model.db.DbModels._Location;
import com.example.david.ermes.Model.db.DbModels._Sport;
import com.example.david.ermes.Model.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 24/05/2017.
 */

public class DatabaseManager {

    private static DatabaseManager instance = new DatabaseManager();

    public static DatabaseManager get() {
        return instance;
    }

    private FirebaseDatabase database;
    private DatabaseReference usersRef, matchesRef, sportsRef, locationsRef;

    private DatabaseManager() {

        this.database = FirebaseDatabase.getInstance();

        this.usersRef = database.getReference("users");
        this.matchesRef = database.getReference("matches");;
        this.sportsRef = database.getReference("sports");
        this.locationsRef = database.getReference("locations");
    }

    public void getCurrentUser(final FirebaseCallback firebaseCallback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            this.usersRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DbModels._User user = dataSnapshot.getValue(DbModels._User.class);
                    user.setUID(dataSnapshot.getKey());

                    firebaseCallback.callback(user);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    firebaseCallback.callback(null);
                }
            });
        } else {
            firebaseCallback.callback(null);
        }
    }

    public DatabaseReference getMatchesRef() {
        return matchesRef;
    }

    public DatabaseReference getUsersRef() {
        return usersRef;
    }

    public DatabaseReference getLocationsRef() { return locationsRef; }

    public DatabaseReference getSportsRef() { return sportsRef; }

    public void fetchAllSports(final FirebaseCallback fc) {
        final List<_Sport> list = new ArrayList<>();
        this.sportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // DbModels._User value = dataSnapshot.getValue(DbModels._User.class);
                list.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    list.add(d.getValue(_Sport.class));
//                    ((_Sport) list.get(list.size() - 1)).setID(d.getKey());
                }
                fc.callback(list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FIREBASE", "Failed to read value.", error.toException());
            }
        });
    }


    public interface OnDataChangedListener<T> {
        void onDataChanged(T data);
    }
}
