package com.example.david.ermes.Model;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.example.david.ermes.Presenter.User;
import com.example.david.ermes.Presenter.Match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David on 24/05/2017.
 */

public class DatabaseManager {

    private DatabaseReference usersRef, matchesRef;

    public DatabaseManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        this.usersRef = database.getReference("users");
        this.usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Models._User value = dataSnapshot.getValue(Models._User.class);
                Log.d("FIREBASE", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FIREBASE", "Failed to read value.", error.toException());
            }
        });

        this.matchesRef = database.getReference("matches");
        this.matchesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Models._Match value = dataSnapshot.getValue(Models._Match.class);
                Log.d("FIREBASE", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FIREBASE", "Failed to read value.", error.toException());
            }
        });
    }

    public User getCurrentUser() {
        User u = null;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoURL = user.getPhotoUrl();
            String UID = user.getUid();

            u = new User(name, email, photoURL, UID);
        }

        return u;
    }

    public void saveUser(String uid, Models._User user) {
        if (user != null) {
            Map<String, Models._User> data = new HashMap<>();
            data.put(uid, user);
            this.usersRef.setValue(data);
        }
    }

    public void saveMatch(Models._Match match) {
        if (match != null) {
            this.matchesRef.push().setValue(match);
        }
    }

    public List<Models._Match> fetchMatchesByIdOwner(String id) {
        final List<Models._Match> matches = new ArrayList<>();

        Query queryRef = this.matchesRef.orderByChild("idOwner").equalTo(id);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Models._Match match = dataSnapshot.getValue(Models._Match.class);
                matches.add(match);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FETCH_MATCHES", "loadMatch:onCancelled", databaseError.toException());
            }
        });

        return matches;
    }

}
