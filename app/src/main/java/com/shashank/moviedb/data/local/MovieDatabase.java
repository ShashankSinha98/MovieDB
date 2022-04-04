package com.shashank.moviedb.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.shashank.moviedb.model.MovieResult;

@Database(entities = {
        MovieResult.class
}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    public abstract MovieDao getMovieDao();
}
