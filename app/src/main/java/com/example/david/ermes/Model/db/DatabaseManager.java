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
    private DatabaseReference usersRef, matchesRef, sportsRef, locationsRef, notificationsRef,
        friendshipRef, teamRef;

    private boolean logged;

    private DatabaseManager() {

        this.database = FirebaseDatabase.getInstance();

        this.usersRef = database.getReference("users");
        this.matchesRef = database.getReference("matches");
        this.sportsRef = database.getReference("sports");
        this.locationsRef = database.getReference("locations");
        this.notificationsRef = database.getReference("notifications");
        this.friendshipRef = database.getReference("friendships");
        this.teamRef = database.getReference("teams");

        logged = false;
    }

    public boolean isLogged(){
        return FirebaseAuth.getInstance().getCurrentUser() != null && logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public void getCurrentUser(final FirebaseCallback firebaseCallback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !user.getUid().isEmpty() && isLogged()) {
            this.usersRef.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DbModels._User u = dataSnapshot.getValue(DbModels._User.class);
                    if ((dataSnapshot.getKey() != null) && (u != null))
                        u.setUID(dataSnapshot.getKey());

                    firebaseCallback.callback(u);
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

    public DatabaseReference getNotificationsRef() { return notificationsRef; }

    public DatabaseReference getFriendshipRef() {
        return friendshipRef;
    }

    public DatabaseReference getTeamRef() { return teamRef; }

    public interface OnDataChangedListener<T> {
        void onDataChanged(T data);
    }
}
