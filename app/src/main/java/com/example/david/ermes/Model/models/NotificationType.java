package com.example.david.ermes.Model.models;

/**
 * Created by nicol on 12/01/2018.
 */

public enum NotificationType {
    MATCH_INVITE_USER,
    FRIENDSHIP_REQUEST,
<<<<<<< HEAD
    FRIENDSHIP_ACCEPTED;
=======
    FRIENDSHIP_ACCEPTED,
    TEAM_ADDED,
    USER_LEAVE_TEAM;
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f

    public static String toString(NotificationType notificationType) {
        String s = null;

        switch (notificationType) {
            case MATCH_INVITE_USER:
                s = "MATCH_INVITE_USER";
                break;
            case FRIENDSHIP_REQUEST:
                s = "FRIENDSHIP_REQUEST";
                break;
            case FRIENDSHIP_ACCEPTED:
                s = "FRIENDSHIP_ACCEPTED";
                break;
<<<<<<< HEAD
=======
            case TEAM_ADDED:
                s = "TEAM_ADDED";
                break;
            case USER_LEAVE_TEAM:
                s = "USER_LEAVE_TEAM";
                break;
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
        }

        return s;
    }

    public static NotificationType getNotificationTypeFromString(String string) {
        NotificationType nt = null;

        switch (string) {
            case "MATCH_INVITE_USER":
                nt = MATCH_INVITE_USER;
                break;
            case "FRIENDSHIP_REQUEST":
                nt = FRIENDSHIP_REQUEST;
                break;
            case "FRIENDSHIP_ACCEPTED":
                nt = FRIENDSHIP_ACCEPTED;
                break;
<<<<<<< HEAD
=======
            case "TEAM_ADDED":
                nt = TEAM_ADDED;
                break;
            case "USER_LEAVE_TEAM":
                nt = USER_LEAVE_TEAM;
                break;
>>>>>>> 7d6df54de0d2ab5df3ce1d6cecfc83157612ce0f
        }

        return nt;
    }
}
