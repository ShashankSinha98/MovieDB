package com.shashank.moviedb.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.shashank.moviedb.data.local.entity.FavouriteMovieIdsEntity;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.data.local.entity.NowPlayingMovieIdsEntity;
import com.shashank.moviedb.data.local.entity.TrendingMovieIdsEntity;

@Database(entities = {
        MovieResult.class,
        TrendingMovieIdsEntity.class,
        NowPlayingMovieIdsEntity.class,
        FavouriteMovieIdsEntity.class
}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao getMovieDao();
}
