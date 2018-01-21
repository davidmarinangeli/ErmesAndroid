package com.example.david.ermes.Model.models;

/**
 * Created by nicol on 08/01/2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.example.david.ermes.Model.db.DbModels._Notification;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.repository.NotificationRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Notification implements Parcelable {

    private String id = null;
    private String idCreator;
    private String idOwner;
    private String idMatch;
    private String title;
    private String text;
    private NotificationType type;
    private boolean read;
    private long date;

    public Notification() {
    }

    public Notification(String id, String idCreator, String idOwner, String idMatch, String title,
                        String text, NotificationType type, boolean read, long date) {
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
                        NotificationType type, boolean read, long date) {
        this.idCreator = idCreator;
        this.idOwner = idOwner;
        this.idMatch = idMatch;
        this.title = title;
        this.text = text;
        this.type = type;
        this.read = read;
        this.date = date;
    }

    protected Notification(Parcel in) {
        id = in.readString();
        idCreator = in.readString();
        idOwner = in.readString();
        idMatch = in.readString();
        title = in.readString();
        text = in.readString();
        type = NotificationType.getNotificationTypeFromString(in.readString());
        read = in.readByte() != 0;
        date = in.readLong();
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

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

    public static Notification createMatchInvitation(String idGuestUser, String idMatch) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (idGuestUser != null && !idGuestUser.isEmpty() &&
                idMatch != null && !idMatch.isEmpty() && user != null) {

            return new Notification(user.getUid(), idGuestUser, idMatch, "Nuovo invito",
                    user.getDisplayName() + " ti ha invitato ad una nuova partita!",
                    NotificationType.MATCH_INVITE_USER, false, System.currentTimeMillis());
        }

        return null;
    }

    public static Notification createFriendshipRequest(String idUser) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (idUser != null && !idUser.isEmpty() && user != null) {
            return new Notification(user.getUid(), idUser, "", "Nuova richiesta di amicizia",
                    user.getDisplayName() + " ti ha inviato una richiesta di amicizia.\n" +
                            "Rispondi subito o visita il suo profilo!",
                    NotificationType.FRIENDSHIP_REQUEST, false, System.currentTimeMillis());
        }

        return null;
    }

    public static Notification createFriendshipAccepted(String idUser) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (idUser != null && !idUser.isEmpty() && user != null) {
            return new Notification(user.getUid(), idUser, "", "Richiesta di amicizia",
                    user.getDisplayName() + " ha accettato la tua richiesta di amicizia, visita" +
                            " il suo profilo!", NotificationType.FRIENDSHIP_ACCEPTED, false,
                    System.currentTimeMillis());
        }

        return null;
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(idCreator);
        parcel.writeString(idOwner);
        parcel.writeString(idMatch);
        parcel.writeString(title);
        parcel.writeString(text);
        parcel.writeString(type.toString());
        parcel.writeByte((byte) (read ? 1 : 0));
        parcel.writeLong(date);
    }
}
