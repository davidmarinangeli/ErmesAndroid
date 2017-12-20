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

    public void fetchSportById(String id, final FirebaseCallback firebaseCallback) {
        SportDatabaseRepository.getInstance().fetchSportById(id, new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                Sport sport = null;

                if (object != null) {
                    sport = ((DbModels._Sport) object).convertToSport();
                }

                firebaseCallback.callback(sport);
            }
        });
    }

    public void fetchAll(final FirebaseCallback firebaseCallback) {
        SportDatabaseRepository.getInstance().fetchAllSports(new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                List<Sport> sports = new ArrayList<>();
                for (DbModels._Sport s : (List<DbModels._Sport>) object) {
                    sports.add(s.convertToSport());
                }
                firebaseCallback.callback(sports);
            }
        });
    }
}
