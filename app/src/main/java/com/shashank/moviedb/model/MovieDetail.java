package com.shashank.moviedb.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.shashank.moviedb.data.local.typeconverter.MovieTypeConverter;

import java.util.List;

@Entity(tableName = "movie_detail")
public class MovieDetail {


    @PrimaryKey @NonNull private Long id;
    @NonNull private String title, overview;
    @ColumnInfo(name="poster_path") @Nullable @SerializedName("poster_path") private String posterPath;
    @ColumnInfo(name="backdrop_path") @Nullable @SerializedName("backdrop_path") private String backdropPath;
    @ColumnInfo(name="vote_average") @Nullable @SerializedName("vote_average") private Double voteAverage;
    @Nullable private Integer runtime;
    @TypeConverters(MovieTypeConverter.class) @Nullable private Credits credits;
    @TypeConverters(MovieTypeConverter.class) @Nullable private List<Genre> genres;
    @Nullable private String tagline;



    public MovieDetail() {}

    public MovieDetail(@NonNull Long id, @NonNull String title, @NonNull String overview,
                       @Nullable String posterPath, @Nullable String backdropPath, @Nullable Double voteAverage, @Nullable Integer runtime, @Nullable Credits credits, @Nullable List<Genre> genres, @Nullable String tagline) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
        this.runtime = runtime;
        this.credits = credits;
        this.genres = genres;
        this.tagline = tagline;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getOverview() {
        return overview;
    }

    public void setOverview(@NonNull String overview) {
        this.overview = overview;
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

    @Nullable
    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(@Nullable Integer runtime) {
        this.runtime = runtime;
    }

    @Nullable
    public Credits getCredits() {
        return credits;
    }

    public void setCredits(@Nullable Credits credits) {
        this.credits = credits;
    }

    @Nullable
    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(@Nullable List<Genre> genres) {
        this.genres = genres;
    }

    @Nullable
    public String getTagline() {
        return tagline;
    }

    public void setTagline(@Nullable String tagline) {
        this.tagline = tagline;
    }

    @Override
    public String toString() {
        return "MovieDetail{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", overview='" + overview + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", voteAverage=" + voteAverage +
                ", runtime=" + runtime +
                ", credits=" + credits +
                ", genres=" + genres +
                ", tagline='" + tagline + '\'' +
                '}';
    }
}
