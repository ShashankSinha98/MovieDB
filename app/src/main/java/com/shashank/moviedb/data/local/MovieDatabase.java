package com.shashank.moviedb.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.shashank.moviedb.data.local.entity.FavouriteMovieIdsEntity;
import com.shashank.moviedb.data.local.typeconverter.MovieTypeConverter;
import com.shashank.moviedb.model.MovieDetail;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.data.local.entity.NowPlayingMovieIdsEntity;
import com.shashank.moviedb.data.local.entity.TrendingMovieIdsEntity;

@Database(entities = {
        MovieResult.class,
        TrendingMovieIdsEntity.class,
        NowPlayingMovieIdsEntity.class,
        FavouriteMovieIdsEntity.class,
        MovieDetail.class
}, version = 1, exportSchema = false)
@TypeConverters(MovieTypeConverter.class)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao getMovieDao();
}
