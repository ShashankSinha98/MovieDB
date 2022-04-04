package com.shashank.moviedb.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.shashank.moviedb.data.local.entity.FavouriteMovieIdsEntity;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.data.local.entity.NowPlayingMovieIdsEntity;
import com.shashank.moviedb.data.local.entity.TrendingMovieIdsEntity;

import java.util.List;

import io.reactivex.Flowable;

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


    @Query("SELECT * FROM trending_movie_ids")
    Flowable<List<TrendingMovieIdsEntity>> getTrendingMovieIds();

    @Query("SELECT * FROM nowplaying_movie_ids")
    Flowable<List<NowPlayingMovieIdsEntity>> getNowPlayingMovieIds();

    @Query("SELECT * FROM movies WHERE id IN (:movieIds)")
    Flowable<List<MovieResult>> getMovieResultsForMovieIds(List<Long> movieIds);

    @Query("SELECT COUNT(*) FROM favourite_movie_ids WHERE movie_id=:movieId")
    Flowable<Integer> getFavouriteCountForMovieId(Long movieId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavouriteMovieId(FavouriteMovieIdsEntity favouriteMovieIdsEntity);

    @Delete
    void deleteFavouriteMovieId(FavouriteMovieIdsEntity favouriteMovieIdsEntity);

    @Query("SELECT * FROM favourite_movie_ids")
    Flowable<List<FavouriteMovieIdsEntity>> getFavouriteMovieIdsEntities();

}