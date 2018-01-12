package com.example.david.ermes.Model.models;

/**
 * Created by nicol on 12/01/2018.
 */

public class NotificationType {
    public static final NotificationType MATCH_INVITE_USER =
            new NotificationType("MATCH_INVITE_USER");

    public static final NotificationType FRIENDSHIP_REQUEST =
            new NotificationType("FRIENDSHIP_REQUEST");


    private String type;

    public NotificationType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
