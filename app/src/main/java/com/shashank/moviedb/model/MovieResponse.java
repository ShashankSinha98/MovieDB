package com.shashank.moviedb.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MovieResponse {

    @NonNull private Integer page;
    @Nullable private List<MovieResult> results;

    public MovieResponse() { }

    public MovieResponse(@NonNull Integer page, @Nullable List<MovieResult> results) {
        this.page = page;
        this.results = results;
    }

    @NonNull
    public Integer getPage() {
        return page;
    }

    public void setPage(@NonNull Integer page) {
        this.page = page;
    }

    @Nullable
    public List<MovieResult> getResults() {
        return results;
    }

    public void setResults(@Nullable List<MovieResult> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "page=" + page +
                ", results=" + results +
                '}';
    }
}
