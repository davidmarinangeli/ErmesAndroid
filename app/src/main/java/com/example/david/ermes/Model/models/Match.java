package com.example.david.ermes.Model.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.david.ermes.Model.db.DbModels;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.repository.MatchRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by David on 30/05/2017.
 * Classe Match per creare una struttura per gli eventi sportivi
 */

public class Match implements Parcelable {
    private String id;

    private String idLocation;
    private String idOwner;
    private String idSport;

    private Date date;
    private boolean isPublic;
    private int maxPlayers;
    private List<String> pending;
    private List<String> idsPartecipants;
    private int numGuests;
    private List<MissingStuffElement> missingStuff;

    public Match() {
        pending = new ArrayList<>();
        idsPartecipants = new ArrayList<>();
        missingStuff = new ArrayList<>();
    }

    public Match(String id, String idOwner, String idLocation, Date date, boolean isPublic,
                 String idSport, int maxPlayers, int numGuests, List<MissingStuffElement> missingStuff,
                 List<String> idsPartecipants, List<String> pending) {
        this.id = id;
        this.idLocation = idLocation;
        this.date = date;
        this.idOwner = idOwner;
        this.isPublic = isPublic;
        this.idSport = idSport;
        this.maxPlayers = maxPlayers;
        this.numGuests = numGuests;
        this.missingStuff = missingStuff != null ? missingStuff : new ArrayList<>();
        this.idsPartecipants = idsPartecipants != null ? idsPartecipants : new ArrayList<>();
        this.pending = pending != null ? pending : new ArrayList<>();
    }

    public Match(String idOwner, String idLocation, Date date, boolean isPublic,
                 String idSport, int maxPlayers, int numGuests, List<MissingStuffElement> missingStuff,
                 List<String> idsPartecipants, List<String> pending) {
        this.idLocation = idLocation;
        this.date = date;
        this.idOwner = idOwner;
        this.isPublic = isPublic;
        this.idSport = idSport;
        this.maxPlayers = maxPlayers;
        this.numGuests = numGuests;
        this.missingStuff = missingStuff != null ? missingStuff : new ArrayList<>();
        this.idsPartecipants = idsPartecipants != null ? idsPartecipants : new ArrayList<>();
        this.pending = pending != null ? pending : new ArrayList<>();
    }

    protected Match(Parcel in) {
        date = new Date(in.readLong());
        idOwner = in.readString();
        isPublic = in.readByte() != 0;
        idSport = in.readString();
        maxPlayers = in.readInt();
        numGuests = in.readInt();
        id = in.readString();
        idLocation = in.readString();

        idsPartecipants = new ArrayList<>();
        pending = new ArrayList<>();
        missingStuff = new ArrayList<>();

        in.readStringList(idsPartecipants);
        in.readStringList(pending);
        in.readTypedList(missingStuff, MissingStuffElement.CREATOR);
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

    public String getIdOwner() {
        return idOwner;
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

    public String getIdLocation() {return this.idLocation; }

    public void setIdLocation(String idLocation) { this.idLocation = idLocation; }

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

    public List<String> getPending() {
        return pending;
    }

    public void setPending(List<String> pending) {
        this.pending = pending;
    }

    public List<String> getPartecipants() {
        return idsPartecipants;
    }

    public void setPartecipants(List<String> partecipants) {
        this.idsPartecipants = partecipants;
    }

    public int getNumGuests() {
        return numGuests;
    }

    public void setNumGuests(int numGuests) {
        this.numGuests = numGuests;
    }

    public List<MissingStuffElement> getMissingStuff() {
        return missingStuff;
    }

    public void setMissingStuff(List<MissingStuffElement> missingStuff) {
        this.missingStuff = missingStuff;
    }

    public String getId() { return this.id; }


    public void addPartecipant(String id) {
        if (!this.idsPartecipants.contains(id)) {
            this.idsPartecipants.add(id);

            this.removePending(id);
        }
    }

    public void removePartecipant(String id) {
        if (this.getPartecipants().contains(id)) {
            this.idsPartecipants.remove(id);
        }
    }

    public void addPending(String id) {
        if (!this.pending.contains(id)) {
            this.pending.add(id);

            this.removePartecipant(id);
        }
    }

    public void removePending(String id) {
        if (this.pending.contains(id)) {
            this.pending.remove(id);
        }
    }

    // repository -> in cui inserire i fetchmatch così come tutti i database manager
    // il repository deve essere un singleton (una sola istanza)

    public DbModels._Match convertTo_Match() {
        DbModels._Match match = new DbModels._Match(
                this.idOwner,
                this.date.getTime(),
                this.idLocation,
                this.isPublic,
                this.idSport,
                this.maxPlayers,
                this.numGuests,
                MissingStuffElement.convertTo_MissingStuffElementList(this.missingStuff),
                this.idsPartecipants,
                this.pending
        );

        match.setID(this.id);
        return match;
    }

    public void save() {
        saveInstance(null);
    }

    public void save(FirebaseCallback firebaseCallback) {
        saveInstance(firebaseCallback);
    }

    private void saveInstance(FirebaseCallback firebaseCallback) {
        MatchRepository.getInstance().saveMatch(this, firebaseCallback);
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
        parcel.writeString(id);
        parcel.writeString(idLocation);
        parcel.writeStringList(idsPartecipants);
        parcel.writeStringList(pending);
        parcel.writeTypedList(missingStuff);
    }
}
