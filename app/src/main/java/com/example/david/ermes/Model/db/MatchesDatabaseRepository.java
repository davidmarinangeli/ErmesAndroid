package com.example.david.ermes.Model.db;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.david.ermes.Model.db.DbModels._Match;
import com.example.david.ermes.Model.db.DatabaseManager.OnDataChangedListener;
import com.example.david.ermes.Model.models.Match;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
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

    public void fetchMatchById(String id, final FirebaseCallback firebaseCallback) {
        fetchMatches("key", id, object -> {
            if (object != null) {
                List<_Match> list = (List<_Match>) object;
                firebaseCallback.callback(list.get(0));
            } else {
                firebaseCallback.callback(null);
            }
        });
    }

    public void push(_Match match, FirebaseCallback firebaseCallback) {
        DatabaseReference query;

        if (match != null) {
            if (match.getID() != null && !match.getID().isEmpty()) {
                query = this.matchesRef.child(match.getID());
            } else {
                query = this.matchesRef.push();
            }

            query.setValue(match, (databaseError, databaseReference) -> {
                if (firebaseCallback != null) {
                    match.setID(databaseReference.getKey());
                    firebaseCallback.callback(match.convertToMatch());
                }
            });
        }
    }

    private void fetchMatches(final String param, String value, final FirebaseCallback fc) {
        Query queryRef;

        if (param == null) {
            queryRef = this.matchesRef.orderByKey();
        } else if (param == "key") {
            queryRef = this.matchesRef.child(value);
        } else {
            queryRef = this.matchesRef.orderByChild(param).equalTo(value);
        }

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // DbModels._User value = dataSnapshot.getValue(DbModels._User.class);
                List<_Match> matches_list = null;
                if (dataSnapshot != null) {
                    matches_list = new ArrayList<>();

                    if (param == "key") {
                        _Match match = dataSnapshot.getValue(_Match.class);
                        match.setID(dataSnapshot.getKey());

                        matches_list.add(match);
                    } else {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            _Match match = d.getValue(_Match.class);
                            match.setID(d.getKey());

                            matches_list.add(match);
                        }
                    }
                }
                fc.callback(matches_list);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                if (fc != null) {
                    fc.callback(null);
                }
            }
        });
    }

    public void orderMatchesByDate(Long date, final FirebaseCallback firebaseCallback) {
        if (date != null) {
            this.matchesRef.orderByChild("date").startAt(date).addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<_Match> matches = null;
                            if (dataSnapshot != null) {
                                matches = new ArrayList<>();
                                for (DataSnapshot d : dataSnapshot.getChildren()) {
                                    _Match match = d.getValue(_Match.class);
                                    match.setID(d.getKey());
                                    if (match.isPublic)
                                        matches.add(match);

                                }
                            }
                            firebaseCallback.callback(matches);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            if (firebaseCallback != null) {
                                firebaseCallback.callback(null);
                            }
                        }
                    });
        } else if (firebaseCallback != null) {
            firebaseCallback.callback(null);
        }
    }

    public void fetchFinishedJoinedMatches(String idUser, FirebaseCallback firebaseCallback) {
        if (idUser != null) {
            Long now = System.currentTimeMillis();

            this.matchesRef.orderByChild("date").endAt(now).addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<_Match> list = null;

                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                _Match match = d.getValue(_Match.class);
                                match.setID(d.getKey());

                                if (match.idOwner == idUser ||
                                        (match.partecipants != null &&
                                                match.partecipants.contains(idUser))) {
                                    if (list == null) {
                                        list = new ArrayList<>();
                                    }

                                    list.add(match);
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
                    }
            );
        } else if (firebaseCallback != null) {
            firebaseCallback.callback(null);
        }
    }

    public void fetchByTimeLapse(long date, String locationId, FirebaseCallback firebaseCallback) {
        final long TWO_HOURS_MILLISEC = 7200000;

        this.matchesRef.orderByChild("date")
                .startAt(date - TWO_HOURS_MILLISEC)
                .endAt(date + TWO_HOURS_MILLISEC)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<_Match> result = null;

                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            _Match match = d.getValue(_Match.class);

                            if (match != null && match.idLocation.equals(locationId)) {
                                if (result == null) {
                                    result = new ArrayList<>();
                                }

                                match.setID(d.getKey());

                                result.add(match);
                            }
                        }

                        if (firebaseCallback != null) {
                            firebaseCallback.callback(result);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if (firebaseCallback != null) {
                            firebaseCallback.callback(null);
                        }
                    }
                });
    }

    public void deleteById(String id, FirebaseCallback firebaseCallback) {
        if (id != null && !id.isEmpty()) {
            this.matchesRef.child(id).removeValue((databaseError, databaseReference) -> {
                if (firebaseCallback != null) {
                    firebaseCallback.callback(null);
                }
            });
        }
    }
}
