package com.example.david.ermes.Model.models;

import com.example.david.ermes.Model.db.DbModels._Friendship;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.repository.FriendshipRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by nicol on 11/01/2018.
 */

public class Friendship {
    public static final String SEPARATOR = "*?*";
    public static final String END = "\uf8ff";

    public static String getFriendshipIdFromIds(String id1, String id2) {
        return id1 + SEPARATOR + id2;
    }

    private String getId1FromId() {
        return this.id.split(Pattern.quote(SEPARATOR))[0];
    }

    private String getId2FromId() {
        return this.id.split(Pattern.quote(SEPARATOR))[1];
    }

    private String id;
    private String id1;
    private String id2;
    private long date;

    public Friendship() {}

    public Friendship(String friendshipId, long date) {
        this.date = date;

        this.id = friendshipId;
        this.id1 = getId1FromId();
        this.id2 = getId2FromId();
    }

    public Friendship(String id1, String id2, long date) {
        this.id1 = id1;
        this.id2 = id2;
        this.date = date;

        this.id = getFriendshipIdFromIds(id1, id2);
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;

        this.id = getFriendshipIdFromIds(id1, this.id2);
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;

        this.id = getFriendshipIdFromIds(this.id1, id2);
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void save() {
        saveInstance(null);
    }

    public void save(FirebaseCallback firebaseCallback) {
        saveInstance(firebaseCallback);
    }

    private void saveInstance(FirebaseCallback firebaseCallback) {
        FriendshipRepository.getInstance().saveFriendship(this, firebaseCallback);
    }

    public _Friendship convertTo_Friendship() {
        return new _Friendship(
                this.id1,
                this.id2,
                this.date
        );
    }

    public static List<_Friendship> convertTo_FriendshipList(List<Friendship> list) {
        List<_Friendship> l = null;

        if (list != null) {
            l = new ArrayList<>();

            for (Friendship f : list) {
                l.add(f.convertTo_Friendship());
            }
        }

        return l;
    }

    public static void requestFriendshipTo(String userID, FirebaseCallback firebaseCallback) {
        Notification.createFriendshipRequest(userID).save(firebaseCallback);
    }

    public static void acceptRequest(Notification notification, FirebaseCallback firebaseCallback) {
        notification.setRead(true);
        notification.save();

        Notification.createFriendshipAccepted(notification.getIdCreator()).save(object -> {
            Notification n = (Notification) object;

            Friendship newFriendship = new Friendship(n.getIdCreator(), n.getIdOwner(),
                    System.currentTimeMillis());
            newFriendship.save(object1 -> {
                if (firebaseCallback != null) {
                    firebaseCallback.callback(newFriendship);
                }
            });
        });
    }
}
