package com.example.david.ermes.Model.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.david.ermes.Model.db.DatabaseManager;
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
    private String photoURL;
    private long birthDate;

    public User() {

    }

    public User(String name, String email, String UID, String city, String idFavSport,
                String photoURL, long birthDate) {
        this.name = name;
        this.email = email;
        this.UID = UID;
        this.city = city;
        this.idFavSport = idFavSport;
        this.photoURL = photoURL;
        this.birthDate = birthDate;
    }

    protected User(Parcel in) {
        name = in.readString();
        email = in.readString();
        UID = in.readString();
        city = in.readString();
        idFavSport = in.readString();
        photoURL = in.readString();
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

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void save() {
        saveInstance(null);
    }

    public void save(FirebaseCallback firebaseCallback) {
        saveInstance(firebaseCallback);
    }

    private void saveInstance(FirebaseCallback firebaseCallback) {
        UserRepository.getInstance().saveUser(this, firebaseCallback);
    }

    public DbModels._User convertTo_User() {
        return new DbModels._User(
                this.name,
                this.email,
                this.idFavSport,
                this.city,
                this.photoURL,
                this.birthDate
        );
    }

    public static String getCurrentUserId() {
        if (currentUser == null) {
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
        }

        if (DatabaseManager.get().isLogged() && currentUser != null) {
            return currentUser.getUid();
        }

        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static int getSportCoverForFavouriteSport(int sportid, Context cx){
        int image_tag = 0;
        switch (sportid) {
            case 0:
                image_tag = cx.getResources().getIdentifier("basketball", "drawable", cx.getPackageName());
                break;
            case 1:
                image_tag = cx.getResources().getIdentifier("volley_cover", "drawable", cx.getPackageName());
                break;
            case 3:
                image_tag = cx.getResources().getIdentifier("tennis", "drawable", cx.getPackageName());
                break;
            case 2:
                image_tag = cx.getResources().getIdentifier("soccer", "drawable", cx.getPackageName());
                break;
            case 4:
                image_tag = cx.getResources().getIdentifier("pingpong", "drawable", cx.getPackageName());
                break;
        }
        return image_tag;
    }

    public static int setImageToSport(Context cx,int sport){
        int image_tag = 0;
        switch (sport) {
            case 0:
                image_tag = cx.getResources().getIdentifier("basketball96", "drawable", cx.getPackageName());
                break;
            case 1:
                image_tag = cx.getResources().getIdentifier("volleyball696", "drawable", cx.getPackageName());
                break;
            case 3:
                image_tag = cx.getResources().getIdentifier("tennisracquet96", "drawable", cx.getPackageName());
                break;
            case 2:
                image_tag = cx.getResources().getIdentifier("soccerball96", "drawable", cx.getPackageName());
                break;
            case 4:
                image_tag = cx.getResources().getIdentifier("pingpong96", "drawable", cx.getPackageName());
                break;
        }
        return image_tag;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(UID);
        parcel.writeString(city);
        parcel.writeString(idFavSport);
        parcel.writeString(photoURL);
        parcel.writeLong(birthDate);
    }
}
