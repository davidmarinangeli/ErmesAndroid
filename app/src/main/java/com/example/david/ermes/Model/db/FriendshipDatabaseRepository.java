package com.example.david.ermes.Model.db;

import android.support.annotation.NonNull;

import com.example.david.ermes.Model.db.DbModels._Friendship;
import com.example.david.ermes.Model.models.Friendship;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 11/01/2018.
 */

public class FriendshipDatabaseRepository {
    private static final FriendshipDatabaseRepository instance = new FriendshipDatabaseRepository();

    public static FriendshipDatabaseRepository getInstance() {
        return instance;
    }


    private static List<_Friendship> results;

    private void resetResults() {
        results = new ArrayList<>();
    }


    private static int fetch_callback_count = 0;

    private int getFetchCallbackCount() {
        return fetch_callback_count;
    }

    private void incrementFetchCallbackCount() {
        fetch_callback_count += 1;
    }

    private void decrementFetchCallbackCount() {
        fetch_callback_count -= 1;
    }

    private void resetFetchCallbackCount() {
        fetch_callback_count = 0;
    }


    private DatabaseReference ref;

    public FriendshipDatabaseRepository() {
        this.ref = DatabaseManager.get().getFriendshipRef();

        resetFetchCallbackCount();
        resetResults();
    }

    public void push(_Friendship friendship, final FirebaseCallback firebaseCallback) {
        this.ref.child(friendship.getId()).setValue(friendship).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (firebaseCallback != null) {
                            firebaseCallback.callback(null);
                        }
                    }
                }
        );
    }

    private void dispatchResults(DataSnapshot dataSnapshot, FirebaseCallback firebaseCallback) {
        incrementFetchCallbackCount();

        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
            for (DataSnapshot d : dataSnapshot.getChildren()) {
                _Friendship f = d.getValue(_Friendship.class);
                f.setId(d.getKey());

                results.add(f);
            }
        }

        if (getFetchCallbackCount() == 2) {
            if (firebaseCallback != null) {
                if (results.size() >= 0) {
                    firebaseCallback.callback(results);
                } else {
                    firebaseCallback.callback(null);
                }
            }

            resetResults();
            resetFetchCallbackCount();
        }
    }

    public void fetchListById(String id, final FirebaseCallback firebaseCallback) {
        resetFetchCallbackCount();
        resetResults();

        this.ref.orderByChild("id1")
                .equalTo(id)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dispatchResults(dataSnapshot, firebaseCallback);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                dispatchResults(null, firebaseCallback);
                            }
                        }
                );

        this.ref.orderByChild("id2")
                .equalTo(id)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                dispatchResults(dataSnapshot, firebaseCallback);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                dispatchResults(null, firebaseCallback);
                            }
                        }
                );
    }
}
