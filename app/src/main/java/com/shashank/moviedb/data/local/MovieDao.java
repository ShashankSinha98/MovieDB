package com.shashank.moviedb.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.shashank.moviedb.model.MovieResult;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

// Using RxJava with Room DB
// @link: https://medium.com/androiddevelopers/room-rxjava-acb0cd4f3757
@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieResults(MovieResult... movieResults);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieResult(MovieResult movieResult);

    @Query("SELECT * FROM movies")
    Flowable<List<MovieResult>> getMovieResults();

    @Query("SELECT COUNT(*) FROM movies")
    Flowable<Integer> getMovieCount();

}