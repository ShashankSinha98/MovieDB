package com.shashank.moviedb.model;

import androidx.annotation.Nullable;

import java.util.List;

public class Credits {

    @Nullable private List<Cast> cast;

    public Credits() {}

    public Credits(@Nullable List<Cast> cast) {
        this.cast = cast;
    }

    @Nullable
    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(@Nullable List<Cast> cast) {
        this.cast = cast;
    }

    @Override
    public String toString() {
        return "Credits{" +
                "cast=" + cast +
                '}';
    }
}
