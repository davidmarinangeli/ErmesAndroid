package com.example.david.ermes.Model.repository;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.db.MatchesDatabaseRepository;
import com.example.david.ermes.Model.models.User;

/**
 * Created by carlo on 30/11/2017.
 */

public class MatchRepository {
    private static final MatchRepository instance = new MatchRepository();

    public static MatchRepository getInstance() {
        return instance;
    }

    private MatchRepository() {

    }

    public void fetchMatchesByIdOwner(User user, FirebaseCallback firebaseCallback) {
        MatchesDatabaseRepository.getInstance().fetchMatchesByIdOwner(user.getUID(), firebaseCallback);
    }
}
