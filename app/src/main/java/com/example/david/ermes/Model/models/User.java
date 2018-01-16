package com.example.david.ermes.Model.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.david.ermes.Model.db.DbModels;
import com.example.david.ermes.Model.db.FirebaseCallback;
import com.example.david.ermes.Model.repository.SportRepository;
import com.example.david.ermes.Model.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by David on 21/07/2017.
 */

public class User implements Parcelable {
    private static FirebaseUser currentUser;

    private String name;
    private String email;
    private String UID;
    private String city;
    private String idFavSport;
    private long birthDate;

    public User() {

    }

    public User(String name, String email, String UID, String city, String idFavSport,
                long birthDate) {
        this.name = name;
        this.email = email;
        this.UID = UID;
        this.city = city;
        this.idFavSport = idFavSport;
        this.birthDate = birthDate;
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        UID = in.readString();
        city = in.readString();
        idFavSport = in.readString();
        birthDate = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUID() {
        return this.UID;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public String getIdFavSport() { return this.idFavSport; }

    public void setIdFavSport(String idFavSport) { this.idFavSport = idFavSport; }

    public long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    public void save() {
        UserRepository.getInstance().saveUser(this);
    }

    public DbModels._User convertTo_User() {
        return new DbModels._User(
                this.name,
                this.email,
                this.idFavSport,
                this.city,
                this.birthDate
        );
    }

    public static String getCurrentUserId() {
        if (currentUser == null) {
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
        }

        if (currentUser != null) {
            return currentUser.getUid();
        }

        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(UID);
        parcel.writeString(city);
        parcel.writeString(idFavSport);
        parcel.writeLong(birthDate);
    }
}
