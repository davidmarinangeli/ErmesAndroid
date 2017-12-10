package com.example.david.ermes.Model.repository;

import com.example.david.ermes.Model.db.DbModels;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.db.SportDatabaseRepository;
import com.example.david.ermes.Model.models.Sport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 04/12/2017.
 */

public class SportRepository {
    private static SportRepository instance = new SportRepository();

    public static SportRepository getInstance() { return instance; }

    public SportRepository() {}

    public void fetchAll(final FirebaseCallback fc) {
        SportDatabaseRepository.getInstance().fetchAllSports(new FirebaseCallback() {
            @Override
            public void callback(List list) {
                List<Sport> sports = new ArrayList<>();
                for (DbModels._Sport s : (List<DbModels._Sport>) list) {
                    sports.add(s.convertToSport());
                }
                fc.callback(sports);
            }
        });
    }
}
