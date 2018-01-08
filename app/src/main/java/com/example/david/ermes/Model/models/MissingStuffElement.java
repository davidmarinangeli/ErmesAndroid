package com.example.david.ermes.Model.models;

/**
 * Created by nicol on 08/01/2018.
 */

import com.example.david.ermes.Model.db.DbModels._MissingStuffElement;

import java.util.ArrayList;
import java.util.List;

public class MissingStuffElement {
    private String name;
    private boolean checked;

    public MissingStuffElement() {
        this.name = "";
        this.checked = false;
    }

    public MissingStuffElement(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }

    public _MissingStuffElement convertTo_MissingStuffElement() {
        return new _MissingStuffElement(
                this.name,
                this.checked
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
}
