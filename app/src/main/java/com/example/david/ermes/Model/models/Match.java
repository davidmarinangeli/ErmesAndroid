package com.example.david.ermes.Model.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.david.ermes.Model.db.DbModels;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.repository.LocationRepository;
import com.example.david.ermes.Model.repository.MatchRepository;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Model.repository.UserRepository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by David on 30/05/2017.
 * Classe Match per creare una struttura per gli eventi sportivi
 */

public class Match implements Serializable, Parcelable {
    private String id;

    private Location location;
    private User owner;
    private Sport sport;

    private String idLocation;
    private String idOwner;
    private String idSport;

    private Date date;
    private boolean isPublic;
    private int maxPlayers;
    private List<User> pending;
    private List<User> partecipants;
    private int numGuests;
    private List<String> missingStuff;

    public Match() {
    }

    public Match(String id, String idOwner, String idLocation, Date date, boolean isPublic,
                 String idSport, int maxPlayers, int numGuests, List<String> missingStuff) {
        this.id = id;
        this.idLocation = idLocation;
        this.date = date;
        this.idOwner = idOwner;
        this.isPublic = isPublic;
        this.idSport = idSport;
        this.maxPlayers = maxPlayers;
        this.numGuests = numGuests;
        this.missingStuff = missingStuff;
    }

    public Match(User owner, Location location, Date date, boolean isPublic,
                 Sport sport, int maxPlayers, int numGuests, List<String> missingStuff) {
        this.location = location;
        this.date = date;
        this.owner = owner;
        this.isPublic = isPublic;
        this.sport = sport;
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

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getId() { return this.id; }

    // repository -> in cui inserire i fetchmatch così come tutti i database manager
    // il repository deve essere un singleton (una sola istanza)

    public void fetchOwner(final FirebaseCallback firebaseCallback) {
        UserRepository.getInstance().fetchUserById(this.idOwner, new FirebaseCallback() {
            @Override
            public void callback(Object object) {
                if (object != null) {
                    owner = (User) object;
                }

                firebaseCallback.callback(owner);
            }
        });
    }

    public void fetchLocation(final FirebaseCallback firebaseCallback) {
        if (this.idLocation != null && this.idLocation.length() > 0) {
            LocationRepository.getInstance().fetchLocationById(this.idLocation, new FirebaseCallback() {
                @Override
                public void callback(Object object) {
                    location = null;
                    if (object != null) {
                        Location loc = (Location) object;

                        idLocation = loc.getId();
                        location = loc;
                    }

                    firebaseCallback.callback(location);
                }
            });
        } else {
            firebaseCallback.callback(null);
        }
    }

    public void fetchSport(final FirebaseCallback firebaseCallback) {
        if (this.idSport != null && this.idSport.length() > 0) {
            SportRepository.getInstance().fetchSportById(this.idSport, new FirebaseCallback() {
                @Override
                public void callback(Object object) {
                    sport = null;
                    if (object != null) {
                        sport = (Sport) object;
                        idSport = sport.getID();
                    }

                    firebaseCallback.callback(sport);
                }
            });
        } else {
            firebaseCallback.callback(null);
        }
    }

    public DbModels._Match convertTo_Match() {
        String id_owner, id_location, id_sport;

        if (this.idOwner != null && this.idOwner.length() > 0) {
            id_owner = this.idOwner;
        } else if (this.owner != null) {
            id_owner = this.owner.getUID();
        } else {
            return null;
        }

        if (this.idLocation != null && this.idLocation.length() > 0) {
            id_location = this.idLocation;
        } else if (this.location != null) {
            id_location = this.location.getId();
        } else {
            return null;
        }

        if (this.idSport != null && this.idSport.length() > 0) {
            id_sport = this.idSport;
        } else if (this.sport != null) {
            id_sport = this.sport.getID();
        } else {
            return null;
        }

        return new DbModels._Match(
                id_owner,
                this.date.getTime(),
                id_location,
                this.isPublic,
                id_sport,
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
