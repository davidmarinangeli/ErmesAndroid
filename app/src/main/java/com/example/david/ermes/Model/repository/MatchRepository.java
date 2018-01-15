package com.example.david.ermes.Model.repository;

import com.example.david.ermes.Model.db.DbModels;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.db.MatchesDatabaseRepository;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.User;

import java.util.List;

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

    public void fetchMatches(final FirebaseCallback firebaseCallback) {
        MatchesDatabaseRepository.getInstance().fetchAllMatches(new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                firebaseCallback.callback(DbModels._Match.convertToMatchList((List<DbModels._Match>) object));
            }
        });
    }

    public void fetchMatchesByOwner(User user, final FirebaseCallback firebaseCallback) {
        MatchesDatabaseRepository.getInstance().fetchMatchesByIdOwner(user.getUID(), new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                firebaseCallback.callback(DbModels._Match.convertToMatchList((List<DbModels._Match>) object));
            }
        });
    }

    public void fetchOrderedMatchesByDate(long date, final FirebaseCallback firebaseCallback) {
        MatchesDatabaseRepository.getInstance().orderMatchesByDate(date, new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                firebaseCallback.callback(DbModels._Match.convertToMatchList((List<DbModels._Match>) object));
            }
        });
    }

    public void fetchMatchById(String id, final FirebaseCallback firebaseCallback) {
        MatchesDatabaseRepository.getInstance().fetchMatchById(id, new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                if (object != null) {
                    firebaseCallback.callback(((DbModels._Match) object).convertToMatch());
                } else {
                    firebaseCallback.callback(null);
                }
            }
        });
    }

    public void saveMatch(Match match, FirebaseCallback firebaseCallback) {
        MatchesDatabaseRepository.getInstance().push(match.convertTo_Match(), firebaseCallback);
    }
}
