package com.example.david.ermes.Presenter;

/**
 * Created by David on 21/07/2017.
 */

public class Account {
    private String username;
    private int age;
    private int exp;
    private String fav_sport;

    //aggiungere altri campi qua sotto

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public String getFav_sport() {
        return fav_sport;
    }

    public void setFav_sport(String fav_sport) {
        this.fav_sport = fav_sport;
    }
}
