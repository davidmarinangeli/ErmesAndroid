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

    private boolean ready, found;
    private int fetchBy2IdsCount = 0;
    private final int MAX_FETCH_BY_2_IDS_COUNT = 2;
    private List<FirebaseCallback> syncList;


    private int fetch_callback_count = 0;
    private int fetch_max_count = 0;
    private void setFetchMaxCount(int value) { fetch_max_count = value; }
    private int getFetchMaxCount() { return fetch_max_count; }
    private int getFetchCallbackCount() {
        return fetch_callback_count;
    }
    private void incrementFetchCallbackCount() {
        fetch_callback_count += 1;
    }
    private void resetFetchCallbackCount() {
        fetch_callback_count = 0;
    }


    private DatabaseReference ref;

    public FriendshipDatabaseRepository() {
        this.ref = DatabaseManager.get().getFriendshipRef();

        resetFetchCallbackCount();
        resetResults();

        syncList = new ArrayList<>();
        ready= true;
        found = false;
    }

    public void push(_Friendship friendship, final FirebaseCallback firebaseCallback) {
        this.ref.child(friendship.getId()).setValue(friendship).addOnCompleteListener(task -> {
                    if (firebaseCallback != null) {
                        firebaseCallback.callback(null);
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

        if (getFetchCallbackCount() == getFetchMaxCount()) {
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
        setFetchMaxCount(2);
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

    private void fetch_by_2_ids(String id_t1, String id_t2, FirebaseCallback firebaseCallback) {
        ready = false;
        found = false;
        fetchBy2IdsCount = 0;

        this.ref.orderByKey().equalTo(id_t1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchBy2IdsCount++;

                _Friendship f = dataSnapshot.getValue(_Friendship.class);
                found = f != null;

                if (firebaseCallback != null && (found || fetchBy2IdsCount == MAX_FETCH_BY_2_IDS_COUNT)) {
                    ready = true;

                    if (syncList.size() > 0) {
                        syncList.get(0).callback(null);
                        syncList.remove(0);
                    }

                    firebaseCallback.callback(f);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        this.ref.orderByKey().equalTo(id_t2).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchBy2IdsCount++;

                _Friendship f = dataSnapshot.getValue(_Friendship.class);
                found = f != null;

                if (firebaseCallback != null && (found || fetchBy2IdsCount == MAX_FETCH_BY_2_IDS_COUNT)) {
                    ready = true;

                    if (syncList.size() > 0) {
                        syncList.get(0).callback(null);
                        syncList.remove(0);
                    }

                    firebaseCallback.callback(f);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void fetchByTwoIds(String id1, String id2, FirebaseCallback firebaseCallback) {
        String id_t1 = Friendship.getFriendshipIdFromIds(id1, id2);
        String id_t2 = Friendship.getFriendshipIdFromIds(id2, id1);

        if (ready) {
            fetch_by_2_ids(id_t1, id_t2, firebaseCallback);
        } else {
            syncList.add(object -> fetch_by_2_ids(id_t1, id_t2, firebaseCallback));
        }
    }
}
