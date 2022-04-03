package com.shashank.moviedb.model;

import androidx.annotation.NonNull;

public class Genre {

    @NonNull private Long id;
    @NonNull private String name;

    public Genre() {}

    public Genre(@NonNull Long id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
}
