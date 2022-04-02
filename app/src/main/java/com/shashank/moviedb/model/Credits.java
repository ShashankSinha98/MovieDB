package com.shashank.moviedb.model;

import androidx.annotation.Nullable;

import java.util.List;

public class Credits {

    @Nullable private List<Actor> cast;

    public Credits() {}

    public Credits(@Nullable List<Actor> cast) {
        this.cast = cast;
    }

    @Nullable
    public List<Actor> getCast() {
        return cast;
    }

    public void setCast(@Nullable List<Actor> cast) {
        this.cast = cast;
    }

    @Override
    public String toString() {
        return "Credits{" +
                "cast=" + cast +
                '}';
    }
}
