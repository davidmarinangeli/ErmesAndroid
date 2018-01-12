package com.example.david.ermes.Model.db;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.example.david.ermes.Model.db.DbModels._Notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nicol on 09/01/2018.
 */

public class NotificationDatabaseRepository {
    private static NotificationDatabaseRepository instance = new NotificationDatabaseRepository();

    public static NotificationDatabaseRepository getInstance() {
        return instance;
    }

    private DatabaseReference ref;

    public NotificationDatabaseRepository() {
        ref = DatabaseManager.get().getNotificationsRef();
    }

    public void fetchByIdOwner(String id, final FirebaseCallback firebaseCallback) {
        this.ref.orderByChild("idOwner").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<_Notification> list = null;

                if (dataSnapshot != null) {
                    list = new ArrayList<>();

                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        _Notification n = d.getValue(_Notification.class);
                        n.setID(d.getKey());

                        list.add(n);
                    }

                    // list sort by date
                    Collections.sort(list, new Comparator<_Notification>() {
                        @Override
                        public int compare(_Notification t1, _Notification t2) {
                            final long date1 = t1.date;
                            final long date2 = t2.date;

                            return date1 > date2 ? 1
                                    : date1 < date2 ? -1
                                    : 0;
                        }
                    });
                }

                firebaseCallback.callback(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                firebaseCallback.callback(null);
            }
        });
    }

    public void push(_Notification notification, final FirebaseCallback firebaseCallback) {
        DatabaseReference query;

        if (notification.getID() != null && !notification.getID().isEmpty()) {
            query = this.ref.child(notification.getID());
        } else {
            query = this.ref.push();
        }

        query.setValue(notification,
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (firebaseCallback != null) {
                            firebaseCallback.callback(null);
                        }
                    }
                });
    }
}
