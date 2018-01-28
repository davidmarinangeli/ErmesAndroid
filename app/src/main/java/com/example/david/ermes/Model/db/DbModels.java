package com.example.david.ermes.Model.db;

import com.example.david.ermes.Model.models.Friendship;
import com.example.david.ermes.Model.models.Location;
import com.example.david.ermes.Model.models.Match;
import com.example.david.ermes.Model.models.Notification;
import com.example.david.ermes.Model.models.NotificationType;
import com.example.david.ermes.Model.models.Sport;
import com.example.david.ermes.Model.models.Team;
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
        public String photoURL;
        public long birthDate;

        private String UID;

        public _User() {
        }

        public _User(String name, String email, String idFavSport, String city, String photoURL,
                     long birthDate) {
            this.name = name;
            this.email = email;
            this.idFavSport = idFavSport;
            this.city = city;
            this.photoURL = photoURL;
            this.birthDate = birthDate;
        }

        public User convertToUser() {
            return new User(
                    this.name,
                    this.email,
                    this.UID,
                    this.city,
                    this.idFavSport,
                    this.photoURL,
                    this.birthDate
            );
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

    public static class _Friendship {
        public long date;
        public String id1;
        public String id2;

        private String id;

        public _Friendship() {}

        public _Friendship(String id1, String id2, long date) {
            this.date = date;
            this.id1 = id1;
            this.id2 = id2;

            this.id = Friendship.getFriendshipIdFromIds(id1, id2);
        }

        public void setId(String id) { this.id = id; }

        public String getId() { return this.id; }

        public Friendship convertToFriendship() {
            return new Friendship(
                    this.id1,
                    this.id2,
                    this.date
            );
        }

        public static List<Friendship> convertToFriendshipList(List<_Friendship> list) {
            List<Friendship> l = null;

            if (list != null) {
                l = new ArrayList<>();

                for (_Friendship f : list) {
                    l.add(f.convertToFriendship());
                }
            }

            return l;
        }
    }

    public static class _MissingStuffElement {
        public String name;
        public boolean checked;
        public String idUser;

        public _MissingStuffElement() {
            this.name = "";
            this.checked = false;
            this.idUser = "";
        }

        public _MissingStuffElement(String name, boolean checked, String idUser) {
            this.name = name;
            this.checked = checked;
            this.idUser = idUser;
        }

        public MissingStuffElement convertToMissingStuffElement() {
            return new MissingStuffElement(
                    this.name,
                    this.checked,
                    this.idUser
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
        public List<String> sportIds;

        private String id;

        public _Location() {
        }

        public _Location(String location, double x, double y, String idUser) {
            this.idUserCreator = idUser;
            this.x = x;
            this.y = y;
            this.location = location;
        }

        public _Location(String location, double x, double y, String idUser, List<String> sportIds) {
            this.idUserCreator = idUser;
            this.x = x;
            this.y = y;
            this.location = location;
            this.sportIds = sportIds;
        }

        public void setID(String id) { this.id = id; }

        public String getID() { return this.id; }

        public Location convertToLocation() {
            return new Location(
                    this.id,
                    this.location,
                    this.x,
                    this.y,
                    this.idUserCreator,
                    this.sportIds
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
        public String idCreator;
        public String idOwner;
        public String idMatch;
        public String idTeam;
        public String title;
        public String text;
        public String type;
        public boolean read;
        public long date;

        private String id;

        public _Notification() {}

        public _Notification(String id, String idCreator, String idOwner, String idMatch, String idTeam,
                             String title, String text, String type, boolean read, long date) {
            this.id = id;
            this.idCreator = idCreator;
            this.idOwner = idOwner;
            this.idMatch = idMatch;
            this.idTeam = idTeam;
            this.title = title;
            this.text = text;
            this.type = type;
            this.read = read;
            this.date = date;
        }

        public void setID(String id) {
            this.id = id;
        }

        public String getID() { return id; }

        public Notification convertToNotification() {
            return new Notification(
                    this.id,
                    this.idCreator,
                    this.idOwner,
                    this.idMatch,
                    this.idTeam,
                    this.title,
                    this.text,
                    NotificationType.getNotificationTypeFromString(this.type),
                    this.read,
                    this.date
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

    public static class _Team {
        public String name;
        public List<String> users;

        private String id;

        public _Team() {}

        public _Team(String id, String name, List<String> userIdList) {
            this.id = id;
            this.name = name;
            this.users = userIdList;
        }

        public void setID(String id) {
            this.id = id;
        }

        public Team convertToTeam() {
            return new Team(
                    this.id,
                    this.name,
                    this.users
            );
        }

        public static List<Team> convertToTeamList(List<_Team> teamList) {
            List<Team> result = new ArrayList<>();

            if (teamList != null) {
                for (_Team t : teamList) {
                    result.add(t.convertToTeam());
                }
            }

            return result;
        }
    }
}
