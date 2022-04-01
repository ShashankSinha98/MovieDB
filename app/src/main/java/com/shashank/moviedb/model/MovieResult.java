package com.shashank.moviedb.model;

public class MovieResult {

    private Long id;
    private String original_title, poster_path, backdrop_path, title;
    private Double vote_average;

    public MovieResult() { }

    public MovieResult(Long id, String original_title, String poster_path, String backdrop_path, String title, Double vote_average) {
        this.id = id;
        this.original_title = original_title;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.title = title;
        this.vote_average = vote_average;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public void setVote_average(Double vote_average) {
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
