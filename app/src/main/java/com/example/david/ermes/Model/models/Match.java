package com.example.david.ermes.Model.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.david.ermes.Model.db.DbModels;
import com.example.david.ermes.Model.repository.MatchRepository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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


    public Match() { }

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

    public DbModels._Match convertTo_Match() {
        return new DbModels._Match(
                this.idOwner,
                this.date.getTime(),
                this.location.convertTo_Location(),
                this.isPublic,
                this.idSport,
                this.maxPlayers,
                this.numGuests,
                this.missingStuff
        );
    }

    public void save() {
        MatchRepository.getInstance().saveMatch(this);
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
