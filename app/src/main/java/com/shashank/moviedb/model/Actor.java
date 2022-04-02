package com.shashank.moviedb.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Actor {

    @NonNull private Long id;
    @NonNull private String name;
    @Nullable @SerializedName("profile_path") private String profilePath;

    public Actor() {}

    public Actor(@NonNull Long id, @NonNull String name, @Nullable String profilePath) {
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
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

    @Nullable
    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(@Nullable String profilePath) {
        this.profilePath = profilePath;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", profile_path='" + profilePath + '\'' +
                '}';
    }
}
