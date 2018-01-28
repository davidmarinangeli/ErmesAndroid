package com.example.david.ermes.Model.db;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.example.david.ermes.Model.db.DbModels._Team;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 26/01/2018.
 */

public class TeamDatabaseRepository {
    private static TeamDatabaseRepository instance = new TeamDatabaseRepository();
    public static TeamDatabaseRepository getInstance() { return instance; }

    private DatabaseReference ref;

    public TeamDatabaseRepository() {
        this.ref = DatabaseManager.get().getTeamRef();
    }

    public void push(String id, _Team team, FirebaseCallback firebaseCallback) {
        DatabaseReference query;

        if (id == null || id.isEmpty()) {
            query = this.ref.push();
        } else {
            query = this.ref.child(id);
        }

        query.setValue(team, (databaseError, databaseReference) -> {
            if (firebaseCallback != null) {
                firebaseCallback.callback(databaseReference.getKey());
            }
        });
    }

    public void fetchByUserId(String id, FirebaseCallback firebaseCallback) {
        this.ref.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<_Team> result = null;

                if (dataSnapshot != null) {
                    result = new ArrayList<>();

                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        _Team t = d.getValue(_Team.class);

                        if (t.users.contains(id)) {
                            t.setID(d.getKey());
                            result.add(t);
                        }
                    }
                }

                if (firebaseCallback != null) {
                    firebaseCallback.callback(result);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void fetchById(String id, FirebaseCallback firebaseCallback) {
        if (id != null && !id.isEmpty()) {
            this.ref.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    _Team team = dataSnapshot.getValue(_Team.class);

                    if (team != null) {
                        team.setID(dataSnapshot.getKey());
                    }

                    if (firebaseCallback != null) {
                        firebaseCallback.callback(team);
                    }
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
}
