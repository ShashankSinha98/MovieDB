package com.shashank.moviedb.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MovieResult {

    @NonNull private Long id;
    @NonNull private String original_title, title;
    @Nullable private String  poster_path, backdrop_path;
    @NonNull private Double vote_average;

    public MovieResult() { }

    public MovieResult(@NonNull Long id, @NonNull String original_title, @NonNull String title, @Nullable String poster_path, @Nullable String backdrop_path, @NonNull Double vote_average) {
        this.id = id;
        this.original_title = original_title;
        this.title = title;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.vote_average = vote_average;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    @NonNull
    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(@NonNull String original_title) {
        this.original_title = original_title;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @Nullable
    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(@Nullable String poster_path) {
        this.poster_path = poster_path;
    }

    @Nullable
    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(@Nullable String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    @NonNull
    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(@NonNull Double vote_average) {
        this.vote_average = vote_average;
    }

    @Override
    public String toString() {
        return "MovieResult{" +
                "id=" + id +
                ", original_title='" + original_title + '\'' +
                ", poster_path='" + poster_path + '\'' +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", title='" + title + '\'' +
                ", vote_average=" + vote_average +
                '}';
    }
}
