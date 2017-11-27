package com.example.david.ermes.Presenter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.example.david.ermes.Model.DatabaseManager;
import com.example.david.ermes.Model.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by David on 30/05/2017.
 * Classe Match per creare una struttura per gli eventi sportivi
 */

public class Match implements Serializable, Parcelable {
    private Location location;
    private Date date;
    private String idOwner;
    private boolean isPublic;
    private String idSport;
    private int maxPlayers;
    private List<User> pending;
    private List<User> partecipants;
    private int numGuests;
    private List<String> missingStuff;

    private DatabaseManager db;

    public Match() { this.db = new DatabaseManager(); }

    public Match(String idOwner, Location location, Date date, boolean isPublic,
                 String idSport, int maxPlayers, int numGuests, List<String> missingStuff) {
        this.location = location;
        this.date = date;
        this.idOwner = idOwner;
        this.isPublic = isPublic;
        this.idSport = idSport;
        this.maxPlayers = maxPlayers;
        this.numGuests = numGuests;
        this.missingStuff = missingStuff;

        this.db = new DatabaseManager();
    }

    protected Match(Parcel in) {
        date = new Date(in.readLong());
        idOwner = in.readString();
        isPublic = in.readByte() != 0;
        idSport = in.readString();
        maxPlayers = in.readInt();
        numGuests = in.readInt();
        missingStuff = in.createStringArrayList();
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    public void save() {
        Models._Location l = new Models._Location(this.location.getName(),
                this.location.getLatitude(),
                this.location.getLongitude(),
                this.location.getLocation_creator().getUID()
        );

        Models._Match m = new Models._Match(this.idOwner, this.date.getTime(), l, this.isPublic,
                this.idSport, this.maxPlayers, this.numGuests, this.missingStuff);
        this.db.saveMatch(m);
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(String idOwner) {
        this.idOwner = idOwner;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getIdSport() {
        return idSport;
    }

    public void setIdSport(String idSport) {
        this.idSport = idSport;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public List<User> getPending() {
        return pending;
    }

    public void setPending(List<User> pending) {
        this.pending = pending;
    }

    public List<User> getPartecipants() {
        return partecipants;
    }

    public void setPartecipants(List<User> partecipants) {
        this.partecipants = partecipants;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public List<String> getMissingStuff() {
        return missingStuff;
    }

    public void setMissingStuff(List<String> missingStuff) {
        this.missingStuff = missingStuff;
    }

    // repository -> in cui inserire i fetchmatch così come tutti i database manager
    // il repository deve essere un singleton (una sola istanza)

    public static void fetchMatchesByIdOwner(String id, final FirebaseCallback fCallback) {
        (new DatabaseManager()).fetchMatches("idOwner", id, new FirebaseCallback() {
            @Override
            public void callback(List list) {
                fCallback.callback(Models._Match.convertToMatchList(list));
            }
        });
    }

    public static void fetchAllMatches(final FirebaseCallback fCallback) {
        (new DatabaseManager()).fetchMatches(null, null, new FirebaseCallback() {
            @Override
            public void callback(List list) {
                fCallback.callback(Models._Match.convertToMatchList(list));
            }
        });
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(date.getTime());
        parcel.writeString(idOwner);
        parcel.writeByte((byte) (isPublic ? 1 : 0));
        parcel.writeString(idSport);
        parcel.writeInt(maxPlayers);
        parcel.writeInt(numGuests);
        parcel.writeStringList(missingStuff);
    }
}
