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
        SportDatabaseRepository.getInstance().fetchSportById(id, object -> {
            Sport sport = null;

            if (object != null) {
                sport = ((DbModels._Sport) object).convertToSport();
            }

            firebaseCallback.callback(sport);
        });
    }

    public void fetchAll(final FirebaseCallback firebaseCallback) {
        SportDatabaseRepository.getInstance().fetchAllSports(object -> {
            List<Sport> sports = new ArrayList<>();
            for (DbModels._Sport s : (List<DbModels._Sport>) object) {
                sports.add(s.convertToSport());
            }
            firebaseCallback.callback(sports);
        });
    }

    public void fetchSportByName(String sport, final FirebaseCallback firebaseCallback) {
        SportDatabaseRepository.getInstance().fetchSportByName(sport, object -> {
            Sport sport1 = null;

            if (object != null) {
                sport1 = ((DbModels._Sport) object).convertToSport();
            }

            firebaseCallback.callback(sport1);
        });
    }
}
