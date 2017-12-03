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
                DbModels._Match value = dataSnapshot.getValue(DbModels._Match.class);
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
        fetchMatches("idOwner", id, new FirebaseCallback() {
            @Override
            public void callback(List list) {
                fCallback.callback(DbModels._Match.convertToMatchList(list));
            }
        });
    }

    public void fetchAllMatches(final FirebaseCallback fCallback) {
        fetchMatches(null, null, new FirebaseCallback() {
            @Override
            public void callback(List list) {
                fCallback.callback(DbModels._Match.convertToMatchList(list));
            }
        });
    }

    public void save(Match match) {
        DbModels._Location l = new DbModels._Location(match.getLocation().getName(),
                match.getLocation().getLatitude(),
                match.getLocation().getLongitude(),
                match.getLocation().getLocation_creator().getUID()
        );

        DbModels._Match m = new DbModels._Match(match.getIdOwner(), match.getDate().getTime(), l, match.isPublic(),
                match.getIdSport(), match.getMaxPlayers(), match.getNumGuests(), match.getMissingStuff());

        this.matchesRef.push().setValue(m);
    }

    private void fetchMatches(String param, String value, final FirebaseCallback fc) {
        Query queryRef = null;

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
                    matches_list.add(match);

                    if (!locations_creators.contains(match.location.idUserCreator)) {
                        locations_creators.add(match.location.idUserCreator);
                    }
                }
                fc.callback(matches_list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FIREBASE", "Failed to read value.", error.toException());
            }
        });

                /*addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                DbModels._Match m = dataSnapshot.getValue(DbModels._Match.class);
                list.add(m);
                Log.d("MATCHES LIST", list.toString());
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
        });*/
    }

}
