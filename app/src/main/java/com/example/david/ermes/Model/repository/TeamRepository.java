package com.example.david.ermes.Model.repository;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.db.TeamDatabaseRepository;
import com.example.david.ermes.Model.models.Team;
import com.example.david.ermes.Model.db.DbModels._Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 26/01/2018.
 */

public class TeamRepository {
    private static TeamRepository instance = new TeamRepository();

    public static TeamRepository getInstance() {
        return instance;
    }

    public TeamRepository() {
    }

    public void saveTeam(Team team, FirebaseCallback firebaseCallback) {
        TeamDatabaseRepository.getInstance().push(team.getId(), team.convertTo_Team(),
                object -> {
                    String ref = (String) object;

                    if (ref != null) {
                        team.setId(ref);
                    }

                    if (firebaseCallback != null) {
                        firebaseCallback.callback(team);
                    }
                });
    }

    public void fetchTeamsByUserId(String userId, FirebaseCallback firebaseCallback) {
        TeamDatabaseRepository.getInstance().fetchByUserId(userId, object -> {
            List<Team> result = null;
            List<_Team> list = (List<_Team>) object;

            if (list != null) {
                result = _Team.convertToTeamList(list);
            }

            if (firebaseCallback != null) {
                firebaseCallback.callback(result);
            }
        });
    }

    public void fetchTeamById(String teamId, FirebaseCallback firebaseCallback) {
        TeamDatabaseRepository.getInstance().fetchById(teamId, object -> {
            _Team t = (_Team) object;
            Team result = null;

            if (t != null) {
                result = t.convertToTeam();
            }

            if (firebaseCallback != null) {
                firebaseCallback.callback(result);
            }
        });
    }
}
