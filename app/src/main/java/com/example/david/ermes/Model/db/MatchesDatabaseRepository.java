package com.example.david.ermes.Model.db;

import android.util.Log;

import com.example.david.ermes.Model.db.DbModels._Match;
import com.example.david.ermes.Model.db.DatabaseManager.OnDataChangedListener;
import com.example.david.ermes.Model.models.Match;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MatchesDatabaseRepository {
    private static final MatchesDatabaseRepository instance = new MatchesDatabaseRepository();

    public static MatchesDatabaseRepository getInstance() {
        return instance;
    }

    private DatabaseReference matchesRef;

    private MatchesDatabaseRepository() {
        DatabaseManager databaseManager = DatabaseManager.get();

        matchesRef = databaseManager.getMatchesRef();

    }

    public void setDataChangedListener(final OnDataChangedListener<Match> listener) {
        matchesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                DbModels._Match value = dataSnapshot.getValue(_Match.class);
                Log.d("FIREBASE", "Value is: " + value);

                listener.onDataChanged(value.convertToMatch());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FIREBASE", "Failed to read value.", error.toException());
            }
        });
    }

    public void fetchMatchesByIdOwner(String id, final FirebaseCallback fCallback) {
        fetchMatches("idOwner", id, fCallback);
    }

    public void fetchAllMatches(final FirebaseCallback fCallback) {
        fetchMatches(null, null, fCallback);
    }

    public void push(_Match match) {
        this.matchesRef.push().setValue(match);
    }

    private void fetchMatches(String param, String value, final FirebaseCallback fc) {
        Query queryRef;

        if (param == null) {
            queryRef = this.matchesRef.orderByKey();
        } else {
            queryRef = this.matchesRef.orderByChild(param).equalTo(value);
        }

        final List<_Match> matches_list = new ArrayList<>();
        final List<String> locations_creators = new ArrayList<>();

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // DbModels._User value = dataSnapshot.getValue(DbModels._User.class);
                matches_list.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    _Match match = d.getValue(_Match.class);
                    match.setId(dataSnapshot.getKey());

                    matches_list.add(match);
                }

                fc.callback(matches_list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FIREBASE", "Failed to read value.", error.toException());
                fc.callback(null);
            }
        });
    }
}
