package com.example.david.ermes.Model;

import com.example.david.ermes.Presenter.FirebaseCallback;
import com.example.david.ermes.Presenter.Location;
import com.example.david.ermes.Presenter.Match;
import com.example.david.ermes.Presenter.Sport;
import com.example.david.ermes.Presenter.User;
import com.example.david.ermes.Presenter.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nicol on 17/10/2017.
 */

public class Models {
    public static class _User {
        public String idFavSport;
        public String city;
        private String name;
        private String email;
        private String UID;

        public _User() {
            this.name = "";
            this.email = "";
            this.UID = "";
        }

        public _User(String idFavSport, String city) {
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

    public static class _Match {
        public long date;
        public _Location location;
        public String idOwner;
        public boolean isPublic;
        public String idSport;
        public int maxPlayers;
        public int numGuests;
        public List<String> missingStuff;

        public _Match() {
        }

        public _Match(String idOwner, long date, _Location location, boolean isPublic,
                      String idSport, int maxPlayers, int numGuests, List<String> missingStuff) {
            this.date = date;
            this.location = location;
            this.idOwner = idOwner;
            this.isPublic = isPublic;
            this.idSport = idSport;
            this.maxPlayers = maxPlayers;
            this.numGuests = numGuests;
            this.missingStuff = missingStuff;
        }

        public Match convertToMatch() {
            System.out.println(this.location);
            return new Match(this.idOwner, this.location.convertToLocation(), TimeUtils.fromMillisToDate(this.date), this.isPublic,
                    this.idSport, this.maxPlayers, this.numGuests, this.missingStuff);
        }

        public static List<Match> convertToMatchList(List<Models._Match> list) {
            List<Match> matches = new ArrayList<>();
            for (Models._Match m : list) {
                matches.add(m.convertToMatch());
            }
            return matches;
        }
    }

    public static class _Sport {
        public String name;
        public int numPlayers;
        private String id;

        public _Sport() {
            this.id = "";
        }

        public _Sport(String name, int numPlayers) {
            this.name = name;
            this.numPlayers = numPlayers;
            this.id = "";
        }

        public Sport convertToSport() {
            return new Sport(
                    this.id,
                    this.name,
                    this.numPlayers
            );
        }

        public static List<Sport> convertToSportList(List<Models._Sport> list) {
            List<Sport> sports = new ArrayList<>();
            for (Models._Sport s : list) {
                sports.add(s.convertToSport());
            }
            return sports;
        }

        public void setID(String id) {
            this.id = id;
        }
    }

    public static class _Location {
        public String location;
        public double x;
        public double y;
        public String idUserCreator;
        private _User userCreator;

        public _Location() {
            this.userCreator = null;
        }

        public _Location(String location, double x, double y, String idUser) {
            this.idUserCreator = idUser;
            this.x = x;
            this.y = y;
            this.location = location;
            this.userCreator = null;
        }

        public Location convertToLocation() {
            User user = null;
            if (this.userCreator != null) {
                user = this.userCreator.convertToUser();
            }

            return new Location(
                    this.location,
                    this.x,
                    this.y,
                    user
            );
        }

        public void setUserCreator(_User user) {
            this.userCreator = user;
        }
    }
}
