package com.example.david.ermes.Model.db;

import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Notification;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.User;
import com.example.david.ermes.Model.models.MissingStuffElement;
import com.example.david.ermes.Presenter.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nicol on 17/10/2017.
 */

public class DbModels {
    public static class _User {
        public String idFavSport;
        public String city;
        public String name;
        public String email;
        private String UID;

        public _User() {
        }

        public _User(String name, String email, String idFavSport, String city) {
            this.name = name;
            this.email = email;
            this.idFavSport = idFavSport;
            this.city = city;
        }

        public User convertToUser() {
            return new User(this.name, this.email, this.UID, this.city, this.idFavSport);
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setUID(String UID) {
            this.UID = UID;
        }

        public String getUID() {return this.UID;}
    }

    public static class _MissingStuffElement {
        public String name;
        public boolean checked;

        public _MissingStuffElement() {
            this.name = "";
            this.checked = false;
        }

        public _MissingStuffElement(String name, boolean checked) {
            this.name = name;
            this.checked = checked;
        }

        public MissingStuffElement convertToMissingStuffElement() {
            return new MissingStuffElement(
                    this.name,
                    this.checked
            );
        }

        public static List<MissingStuffElement> convertToMissingStuffElementList(List<_MissingStuffElement> list) {
            List<MissingStuffElement> l = null;

            if (list != null) {
                l = new ArrayList<>();

                for (_MissingStuffElement m : list) {
                    l.add(m.convertToMissingStuffElement());
                }
            }

            return l;
        }
    }

    public static class _Match {
        public long date;
        public String idLocation;
        public String idOwner;
        public boolean isPublic;
        public String idSport;
        public int maxPlayers;
        public int numGuests;
        public List<_MissingStuffElement> missingStuff;
        public List<String> partecipants;
        public List<String> pending;

        private String id;

        public _Match() {
        }

        public _Match(String idOwner, long date, String idLocation, boolean isPublic,
                      String idSport, int maxPlayers, int numGuests, List<_MissingStuffElement> missingStuff,
                      List<String> partecipants, List<String> pending) {
            this.date = date;
            this.idLocation = idLocation;
            this.idOwner = idOwner;
            this.isPublic = isPublic;
            this.idSport = idSport;
            this.maxPlayers = maxPlayers;
            this.numGuests = numGuests;
            this.missingStuff = missingStuff;
            this.partecipants = partecipants;
            this.pending = pending;
        }

        public void setID(String id) { this.id = id; }

        public String getID() { return this.id; }

        public Match convertToMatch() {
            if (this.id == null || this.id.length() <= 0) {
                return null;
            }

            return new Match(
                    this.id,
                    this.idOwner,
                    this.idLocation,
                    TimeUtils.fromMillisToDate(this.date),
                    this.isPublic,
                    this.idSport,
                    this.maxPlayers,
                    this.numGuests,
                    _MissingStuffElement.convertToMissingStuffElementList(this.missingStuff),
                    this.partecipants,
                    this.pending
            );
        }

        public static List<Match> convertToMatchList(List<DbModels._Match> list) {
            List<Match> matches = new ArrayList<>();
            for (DbModels._Match m : list) {
                matches.add(m.convertToMatch());
            }
            return matches;
        }
    }

    public static class _Sport {

        public String name;
        public int numPlayers;

        private String id;

        public _Sport(String name, int numPlayers) {
            this.name = name;
            this.numPlayers = numPlayers;
        }

        public _Sport() {
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setID(String id) {
            this.id = id;
        }

        public String getID() { return this.id; }

        public Sport convertToSport() {
            return new Sport(
                    this.id,
                    this.name,
                    this.numPlayers
            );
        }

        public static List<Sport> convertToSportList(List<DbModels._Sport> list) {
            List<Sport> sports = new ArrayList<>();
            for (DbModels._Sport s : list) {
                sports.add(s.convertToSport());
            }
            return sports;
        }
    }

    public static class _Location {
        public String location;
        public double x;
        public double y;
        public String idUserCreator;

        private String id;

        public _Location() {
        }

        public _Location(String location, double x, double y, String idUser) {
            this.idUserCreator = idUser;
            this.x = x;
            this.y = y;
            this.location = location;
        }

        public void setID(String id) { this.id = id; }

        public String getID() { return this.id; }

        public Location convertToLocation() {
            return new Location(
                    this.id,
                    this.location,
                    this.x,
                    this.y,
                    this.idUserCreator
            );
        }

        public static List<Location> convertToLocationList(List<DbModels._Location> list) {
            List<Location> locations = new ArrayList<>();

            for (DbModels._Location l : list) {
                locations.add(l.convertToLocation());
            }

            return locations;
        }
    }

    public static class _Notification {
        public String idOwner;
        public String title;
        public String text;
        public String type;
        public boolean read;

        private String id;

        public _Notification() {}

        public _Notification(String id, String idOwner, String title, String text, String type,
                             boolean read) {
            this.id = id;
            this.idOwner = idOwner;
            this.title = title;
            this.text = text;
            this.type = type;
            this.read = read;
        }

        public void setID(String id) {
            this.id = id;
        }

        public String getID() { return id; }

        public Notification convertToNotification() {
            return new Notification(
                    this.id,
                    this.idOwner,
                    this.title,
                    this.text,
                    this.type,
                    this.read
            );
        }

        public static List<Notification> convertToNotificationList(List<_Notification> list) {
            List<Notification> l = new ArrayList<>();

            for (_Notification n : list) {
                l.add(n.convertToNotification());
            }

            return l;
        }
    }
}
