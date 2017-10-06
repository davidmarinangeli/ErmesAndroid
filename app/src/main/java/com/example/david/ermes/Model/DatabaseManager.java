package com.example.david.ermes.Model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.david.ermes.Presenter.Account;
import com.example.david.ermes.Presenter.Match;

/**
 * Created by David on 24/05/2017.
 */

public class DatabaseManager {

    private DatabaseReference users, matches;

    public DatabaseManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        users = database.getReference("users");
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("FIREBASE", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FIREBASE", "Failed to read value.", error.toException());
            }
        });

        matches = database.getReference("matches");
        matches.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("FIREBASE", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FIREBASE", "Failed to read value.", error.toException());
            }
        });

        users.setValue("Write test");
    }

    public void saveUser(Account account) {
        users.setValue(account);
    }

    public void saveMatch(Match match) {
        matches.setValue(match);
    }

}
