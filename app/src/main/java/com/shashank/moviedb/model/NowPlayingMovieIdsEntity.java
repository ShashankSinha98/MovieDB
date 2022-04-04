package com.shashank.moviedb.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "nowplaying_movie_ids")
public class NowPlayingMovieIdsEntity {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "movie_id")
    private Long movieId;

    public NowPlayingMovieIdsEntity(Long movieId) {
        this.movieId = movieId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
}
