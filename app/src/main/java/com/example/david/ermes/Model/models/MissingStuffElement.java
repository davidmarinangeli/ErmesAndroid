package com.example.david.ermes.Model.models;

/**
 * Created by nicol on 08/01/2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.example.david.ermes.Model.db.DbModels._MissingStuffElement;

import java.util.ArrayList;
import java.util.List;

public class MissingStuffElement implements Parcelable {
    public static final Creator<MissingStuffElement> CREATOR = new Creator<MissingStuffElement>() {
        @Override
        public MissingStuffElement createFromParcel(Parcel in) {
            return new MissingStuffElement(in);
        }

        @Override
        public MissingStuffElement[] newArray(int size) {
            return new MissingStuffElement[size];
        }
    };

    protected MissingStuffElement(Parcel in) {
        name = in.readString();
        checked = in.readByte() != 0;
        idUser = in.readString();
    }

    private String name;
    private boolean checked;
    private String idUser;

    public MissingStuffElement() {
        this.name = "";
        this.checked = false;
        this.idUser = "";
    }

    public MissingStuffElement(String name, boolean checked, String idUser) {
        this.name = name;
        this.checked = checked;
        this.idUser = idUser;
    }

    public _MissingStuffElement convertTo_MissingStuffElement() {
        return new _MissingStuffElement(
                this.name,
                this.checked,
                this.idUser
        );
    }

    public static List<_MissingStuffElement> convertTo_MissingStuffElementList(List<MissingStuffElement> list) {
        List<_MissingStuffElement> l = null;

        if (list != null) {
            l = new ArrayList<>();

            for (MissingStuffElement m : list) {
                l.add(m.convertTo_MissingStuffElement());
            }
        }

        return l;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeByte((byte) (checked ? 1 : 0));
        parcel.writeString(idUser);
    }
}
