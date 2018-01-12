package com.example.david.ermes.Model.models;

/**
 * Created by nicol on 12/01/2018.
 */

public class NotificationType {
    public static final NotificationType MATCH_INVITE_USER =
            new NotificationType("MATCH_INVITE_USER");

    public static final NotificationType FRIENDSHIP_REQUEST =
            new NotificationType("FRIENDSHIP_REQUEST");

    public static final NotificationType FRIENDSHIP_ACCEPTED =
            new NotificationType("FRIENDSHIP_ACCEPTED");


    private String type;

    public NotificationType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) return false;

        if (object.getClass() != NotificationType.class) return false;

        final NotificationType obj = (NotificationType) object;
        return this.toString().equals(obj.toString());
    }
}
