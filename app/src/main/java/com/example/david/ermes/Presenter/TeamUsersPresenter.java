package com.example.david.ermes.Presenter;

import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.models.Team;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 26/01/2018.
 */

public class TeamUsersPresenter {
    private int fetch_count = 0;
    private int MAX_FETCH_COUNT = 2;

    private void setMAX_FETCH_COUNT(int value) {
        MAX_FETCH_COUNT = value;
    }

    private void incrementFetchCount(Team team) {
        fetch_count++;

        if (fetch_count == MAX_FETCH_COUNT) {
            fetch_count = 0;

            if (firebaseCallback != null) {
                firebaseCallback.callback(getMembersStringValue(team));
            }
        }
    }

    private void resetFetchCount() {
        fetch_count = 0;
    }

    private List<User> userList;
    private FirebaseCallback firebaseCallback;

    public TeamUsersPresenter() {
        userList = new ArrayList<>();
    }

    public void calculateMembersStringValue(Team team, FirebaseCallback firebaseCallback) {
        this.firebaseCallback = firebaseCallback;
        resetFetchCount();
        setMAX_FETCH_COUNT(team.getUserIdList().size());

        for (String id : team.getUserIdList()) {
            UserRepository.getInstance().fetchUserById(id, object -> {
                User user = (User) object;

                if (user != null) {
                    userList.add(user);
                }

                incrementFetchCount(team);
            });
        }
    }

    private String getMembersStringValue(Team team) {
        String s = "";

        if (userList != null) {
            if (userList.isEmpty()) {
                s = "Non ci sono membri";
            } else if (userList.size() > 3) {
                String name1 = !userList.get(0).getUID().equals(User.getCurrentUserId()) ?
                        userList.get(0).getName() : "Tu";

                String name2 = !userList.get(1).getUID().equals(User.getCurrentUserId()) ?
                        userList.get(1).getName() : "Tu";

                s = name1 + ", " + name2 + " e altri " + (team.getUserIdList().size() - 2) +
                        " membri";
            } else {
                s = "";

                for (int i = 0; i < userList.size(); i++) {
                    String append = !userList.get(i).getUID().equals(User.getCurrentUserId()) ?
                            userList.get(i).getName() : "Tu";

                    if (i < userList.size() - 2) {
                        append += ", ";
                    } else if (i < userList.size() - 1) {
                        append += " e ";
                    }

                    s += append;
                }
            }
        }

        return s;
    }

}
