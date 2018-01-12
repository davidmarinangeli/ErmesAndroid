package com.example.david.ermes.Model.models;

/**
 * Created by nicol on 12/01/2018.
 */

public enum NotificationType {
    MATCH_INVITE_USER,
    FRIENDSHIP_REQUEST,
    FRIENDSHIP_ACCEPTED;

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
        }

        return nt;
    }
}
