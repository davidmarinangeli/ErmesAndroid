package com.example.david.ermes.Model.models;

/**
 * Created by nicol on 08/01/2018.
 */

import com.example.david.ermes.Model.db.DbModels._Notification;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

public class Notification {

    private String id = null;
    private String idCreator;
    private String idOwner;
    private String idMatch;
    private String title;
    private String text;
    private NotificationType type;
    private boolean read;
    private int date;

    public Notification() {}

    public Notification(String id, String idCreator, String idOwner, String idMatch, String title,
                        String text, NotificationType type, boolean read, int date) {
        this.id = id;
        this.idCreator = idCreator;
        this.idOwner = idOwner;
        this.idMatch = idMatch;
        this.title = title;
        this.text = text;
        this.type = type;
        this.read = read;
        this.date = date;
    }

    public Notification(String idCreator, String idOwner, String idMatch, String title, String text,
                        NotificationType type, boolean read, int date) {
        this.idCreator = idCreator;
        this.idOwner = idOwner;
        this.idMatch = idMatch;
        this.title = title;
        this.text = text;
        this.type = type;
        this.read = read;
        this.date = date;
    }

    public void save() {
        saveInstance(null);
    }

    public void save(FirebaseCallback firebaseCallback) {
        saveInstance(firebaseCallback);
    }

    private void saveInstance(FirebaseCallback firebaseCallback) {
        NotificationRepository.getInstance().sendNotification(
                this, firebaseCallback);
    }

    public _Notification convertTo_Notification() {
        return new _Notification(
                this.id,
                this.idCreator,
                this.idOwner,
                this.idMatch,
                this.title,
                this.text,
                this.type.toString(),
                this.read,
                this.date
        );
    }

    public static List<_Notification> convertTo_NotificationList(List<Notification> list) {
        List<_Notification> l = new ArrayList<>();

        for (Notification n : list) {
            l.add(n.convertTo_Notification());
        }

        return l;
    }

    public static List<Notification> getReadNotificationsFromList(List<Notification> list) {
        List<Notification> l = new ArrayList<>();

        for (Notification n : list) {
            if (n.isRead()) {
                l.add(n);
            }
        }

        return l;
    }

    public static List<Notification> getUnreadNotificationsFromList(List<Notification> list) {
        List<Notification> l = new ArrayList<>();

        for (Notification n : list) {
            if (!n.isRead()) {
                l.add(n);
            }
        }

        return l;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public String getIdCreator() {
        return idCreator;
    }

    public void setIdCreator(String idCreator) {
        this.idCreator = idCreator;
    }

    public String getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(String idMatch) {
        this.idMatch = idMatch;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }
}
