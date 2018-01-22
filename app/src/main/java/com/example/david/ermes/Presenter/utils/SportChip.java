package com.example.david.ermes.Presenter.utils;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tylersuehr.chips.data.Chip;

/**
 * Created by david on 1/12/2018.
 */

public class SportChip extends Chip {
    private int id;
    private String name;

    public SportChip() {}

    public SportChip(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Nullable
    @Override
    public Integer getId() {
        return id;
    }

    @NonNull
    @Override
    public String getTitle() {
        return name;
    }

    @Nullable
    @Override
    public String getSubtitle() {
        return null;
    }

    @Nullable
    @Override
    public Uri getAvatarUri() {
        return null;
    }

    @Nullable
    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
