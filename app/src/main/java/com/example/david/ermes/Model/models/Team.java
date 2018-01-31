package com.example.david.ermes.Model.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.david.ermes.Model.db.DbModels._Team;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 26/01/2018.
 */

public class Team implements Parcelable {
    private String id;
    private String name;
    private List<String> userIdList;

    public Team() {
        userIdList = new ArrayList<>();
    }

    public Team(String name, List<String> userIdList) {
        this.name = name;
        this.userIdList = userIdList;
    }

    public Team(String id, String name, List<String> userIdList) {
        this.id = id;
        this.name = name;
        this.userIdList = userIdList;
    }

    public Team(Parcel parcel) {
        id = parcel.readString();
        name = parcel.readString();

        userIdList = new ArrayList<>();
        parcel.readStringList(userIdList);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public void addUser(String userId) {
        if (userIdList != null && !userIdList.contains(userId)) {
            userIdList.add(userId);
        }
    }

    public void removeUser(String userId) {
        if (userIdList != null) {
            userIdList.remove(userId);
        }
    }

    public void save() {
        saveInstance(null);
    }

    public void save(FirebaseCallback firebaseCallback) {
        saveInstance(firebaseCallback);
    }

    private void saveInstance(FirebaseCallback firebaseCallback) {
        TeamRepository.getInstance().saveTeam(this, object -> {
            Team team = (Team) object;

            if (team != null) {
                this.setId(team.getId());
                this.setName(team.getName());
                this.setUserIdList(team.getUserIdList());
            }

            if (firebaseCallback != null) {
                firebaseCallback.callback(team);
            }
        });
    }

    public _Team convertTo_Team() {
        return new _Team(
                this.id,
                this.name,
                this.userIdList
        );
    }

    public static List<_Team> convertTo_TeamList(List<Team> teamList) {
        List<_Team> result = new ArrayList<>();

        if (teamList != null) {
            for (Team t : teamList) {
                result.add(t.convertTo_Team());
            }
        }

        return result;
    }

    public static final Creator<Team> CREATOR = new Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeStringList(userIdList);
    }
}
