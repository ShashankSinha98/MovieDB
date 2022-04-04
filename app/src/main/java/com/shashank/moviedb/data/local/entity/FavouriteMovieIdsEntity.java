package com.shashank.moviedb.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_movie_ids")
public class FavouriteMovieIdsEntity {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "movie_id")
    private Long movieId;

    public FavouriteMovieIdsEntity(Long movieId) {
        this.movieId = movieId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
}
