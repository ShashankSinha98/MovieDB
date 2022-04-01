package com.shashank.moviedb.model;

import java.util.List;

public class MovieResponse {

    private int page;
    private List<MovieResult> results;

    public MovieResponse() { }

    public MovieResponse(int page, List<MovieResult> results) {
        this.page = page;
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieResult> getResults() {
        return results;
    }

    public void setResults(List<MovieResult> results) {
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
