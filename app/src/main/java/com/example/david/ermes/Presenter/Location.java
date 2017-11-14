package com.example.david.ermes.Presenter;

/**
 * Created by david on 11/14/2017.
 */

public class Location {
    private User location_creator;
    private Long latitude;
    private Long longitude;
    private String name;

    public User getLocation_creator() {
        return location_creator;
    }

    public void setLocation_creator(User location_creator) {
        this.location_creator = location_creator;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
