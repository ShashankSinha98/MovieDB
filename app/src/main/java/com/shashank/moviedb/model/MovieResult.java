package com.shashank.moviedb.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "movies")
public class MovieResult {

    @PrimaryKey(autoGenerate = true)
    @NonNull private Long id;

    @ColumnInfo(name = "original_title")
    @NonNull @SerializedName("original_title") private String originalTitle;

    @ColumnInfo(name = "title")
    @NonNull private String title;

    @ColumnInfo(name = "poster_path")
    @Nullable @SerializedName("poster_path") private String posterPath;

    @ColumnInfo(name = "backdrop_path")
    @Nullable @SerializedName("backdrop_path") private String backdropPath;

    @ColumnInfo(name = "vote_average")
    @Nullable @SerializedName("vote_average") private Double voteAverage;


    public MovieResult() { }

    public MovieResult(@NonNull Long id, @NonNull String originalTitle, @NonNull String title, @Nullable String posterPath,
                       @Nullable String backdropPath, @Nullable Double voteAverage) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    @NonNull
    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(@NonNull String originalTitle) {
        this.originalTitle = originalTitle;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @Nullable
    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(@Nullable String posterPath) {
        this.posterPath = posterPath;
    }

    @Nullable
    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(@Nullable String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @Nullable
    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(@Nullable Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public String toString() {
        return "MovieResult{" +
                "id=" + id +
                ", original_title='" + originalTitle + '\'' +
                ", poster_path='" + posterPath + '\'' +
                ", backdrop_path='" + backdropPath + '\'' +
                ", title='" + title + '\'' +
                ", vote_average=" + voteAverage +
                '}';
    }
}
