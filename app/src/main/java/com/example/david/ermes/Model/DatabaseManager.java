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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.david.ermes.Presenter.FirebaseCallback;

/**
 * Created by David on 24/05/2017.
 */

public class DatabaseManager {

    private DatabaseReference usersRef, matchesRef, sportsRef;

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

        this.sportsRef = database.getReference("sports");
    }

    public User getCurrentUser() {
        User u = null;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            String UID = user.getUid();

            u = new User(name, email, UID, "", "sport a caso");
        }

        return u;
    }

    public void saveUser(String uid, Models._User user) {
        if (user != null) {
            this.usersRef.child(uid).setValue(user);
        }
    }

    public void saveMatch(Models._Match match) {
        if (match != null) {
            this.matchesRef.push().setValue(match);
        }
    }

    public void fetchUserById(String id, final FirebaseCallback fc) {
        final List<Models._User> list = new ArrayList<>();
        Query getUser = this.usersRef.equalTo(id);
        getUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.add(dataSnapshot.getValue(Models._User.class));
                fc.callback(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void fetchUsersById(final List<String> idList, final FirebaseCallback fc) {
        final List<Models._User> list = new ArrayList<>();
        Query getUsers = this.usersRef.orderByKey();
        getUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    if (idList.contains(d.getKey().toString())) {
                        Models._User user = d.getValue(Models._User.class);
                        user.setUID(d.getKey());
                        list.add(user);
                    }
                }
                fc.callback(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void fetchAllSports(final FirebaseCallback fc) {
        final List<Models._Sport> list = new ArrayList<>();
        this.sportsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // Models._User value = dataSnapshot.getValue(Models._User.class);
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    list.add(d.getValue(Models._Sport.class));
                    ((Models._Sport) list.get(list.size() - 1)).setID(d.getKey());
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

    public void fetchMatches(String param, String value, final FirebaseCallback fc) {

        Query queryRef = this.matchesRef.orderByChild(param).equalTo(value);
        final List<Models._Match> matches_list = new ArrayList<>();
        final List<String> locations_creators = new ArrayList<>();

        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // Models._User value = dataSnapshot.getValue(Models._User.class);
                for (DataSnapshot d: dataSnapshot.getChildren()) {
                    Models._Match match = d.getValue(Models._Match.class);
                    matches_list.add(match);

                    if (!locations_creators.contains(match.location.idUserCreator)) {
                        locations_creators.add(match.location.idUserCreator);
                    }
                }

                fetchUsersById(locations_creators, new FirebaseCallback() {
                    @Override
                    public void callback(List list) {
                        for (Models._Match match : matches_list) {
                            if (locations_creators.contains(match.location.idUserCreator)) {
                                for (Models._User user : (List<Models._User>) list) {
                                    if (match.location.idUserCreator == user.getUID()) {
                                        match.location.setUserCreator(user);
                                    }
                                }
                            }
                        }
                        fc.callback(matches_list);
                    }
                });
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
                Models._Match m = dataSnapshot.getValue(Models._Match.class);
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
