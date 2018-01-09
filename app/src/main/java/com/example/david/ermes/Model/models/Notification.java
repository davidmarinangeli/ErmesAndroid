package com.example.david.ermes.Model.models;

/**
 * Created by nicol on 08/01/2018.
 */

import com.example.david.ermes.Model.db.DbModels._Notification;
import com.example.david.ermes.Model.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

public class Notification {
    public static class types {
        public static final String MATCH_INVITE_USER = "MATCH_INVITE_USER";
    }

    private String id = null;
    private String idOwner;
    private String title;
    private String text;
    private String type;
    private boolean read;

    public Notification() {}

    public Notification(String id, String idOwner, String title, String text, String type,
                        boolean read) {
        this.id = id;
        this.idOwner = idOwner;
        this.title = title;
        this.text = text;
        this.type = type;
        this.read = read;
    }

    public Notification(String idOwner, String title, String text, String type, boolean read) {
        this.idOwner = idOwner;
        this.title = title;
        this.text = text;
        this.type = type;
        this.read = read;
    }

    public void save() {
        NotificationRepository.getInstance().saveNotification(this);
    }

    public _Notification convertTo_Notification() {
        return new _Notification(
                this.id,
                this.idOwner,
                this.title,
                this.text,
                this.type,
                this.read
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
