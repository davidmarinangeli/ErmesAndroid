package com.example.david.ermes.Model.db;

import android.util.Log;

import com.example.david.ermes.Model.db.DbModels._Sport;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 04/12/2017.
 */

public class SportDatabaseRepository {
    private static SportDatabaseRepository instance = new SportDatabaseRepository();

    public static SportDatabaseRepository getInstance() { return instance; }

    private DatabaseReference ref;

    public SportDatabaseRepository() {
        this.ref = DatabaseManager.get().getSportsRef();
    }

    public void fetchAllSports(final FirebaseCallback fc) {
        final List<_Sport> list = new ArrayList<>();
        this.ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                // DbModels._User value = dataSnapshot.getValue(DbModels._User.class);
                list.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    list.add(d.getValue(_Sport.class));
//                    list.get(list.size() - 1).setID(d.getKey().toString());
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
}
