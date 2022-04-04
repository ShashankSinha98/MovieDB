package com.shashank.moviedb.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.model.NowPlayingMovieIdsEntity;
import com.shashank.moviedb.model.TrendingMovieIdsEntity;

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
    void insertTrendingMovieIds(TrendingMovieIdsEntity... trendingMovieIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNowPlayingMovieIds(NowPlayingMovieIdsEntity... trendingMovieIds);


    @Query("SELECT * FROM movies")
    Flowable<List<MovieResult>> getMovieResults();

    @Query("SELECT * FROM trending_movie_ids")
    Flowable<List<TrendingMovieIdsEntity>> getTrendingMovieIds();

    @Query("SELECT * FROM nowplaying_movie_ids")
    Flowable<List<NowPlayingMovieIdsEntity>> getNowPlayingMovieIds();

    @Query("SELECT * FROM movies WHERE id IN (:movieIds)")
    Flowable<List<MovieResult>> getMovieResultsForMovieIds(List<Long> movieIds);

}