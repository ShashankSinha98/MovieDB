package com.shashank.moviedb.data.remote;

import static com.shashank.moviedb.util.Constants.TRENDING_TIME_WINDOW;

import android.util.Log;

import androidx.annotation.NonNull;

import com.shashank.moviedb.data.ResourceCallback;
import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.model.MovieDetail;
import com.shashank.moviedb.model.MovieResponse;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.util.Constants;
import com.shashank.moviedb.util.Constants.QueryParams;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieRepositoryImpl implements MovieRepository {

    private static final String TAG = "MovieRepositoryImpl";

    private MovieApi movieApi;

    @Inject
    public MovieRepositoryImpl(MovieApi movieApi) {
        this.movieApi = movieApi;
    }


    @Override
    public void fetchNowPlayingMovies(ResourceCallback resourceCallback) {

        movieApi.fetchNowPlayingMovie(QueryParams.PARAMS_NOW_PLAYING_MOVIE_API)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG,"fetchNowPlayingMovies - onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull MovieResponse movieResponse) {
                        Log.d(TAG,"fetchNowPlayingMovies - onNext: movieResponse: "+movieResponse);
                        resourceCallback.onResponse(Resource.success(movieResponse));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "fetchNowPlayingMovies - onError: e: "+e.getMessage());
                        resourceCallback.onResponse(Resource.error(e.getMessage(), null));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "fetchNowPlayingMovies - onComplete");
                    }
                });
    }


    @Override
    public void fetchTrendingMovies(ResourceCallback resourceCallback) {

        movieApi.fetchTrendingMovie(TRENDING_TIME_WINDOW, QueryParams.PARAMS_TRENDING_MOVIE_API)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG,"fetchTrendingMovies - onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull MovieResponse movieResponse) {
                        Log.d(TAG,"fetchTrendingMovies - onNext: movieResponse: "+movieResponse);
                        resourceCallback.onResponse(Resource.success(movieResponse));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "fetchTrendingMovies - onError: e: "+e.getMessage());
                        resourceCallback.onResponse(Resource.error(e.getMessage(), null));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "fetchTrendingMovies - onComplete");
                    }
                });
    }


    @Override
    public void fetchMovieDetail(Long movieId, ResourceCallback resourceCallback) {

        if(movieId==null || movieId<=0L) {
            resourceCallback.onResponse(Resource.error(Constants.INVALID_MOVIE_ID_ERROR_MSG,null));
            return;
        }

        movieApi.fetchMovieDetail(movieId, QueryParams.PARAMS_MOVIE_DETAIL_API)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieDetail>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG,"fetchMovieDetail - onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull MovieDetail movieDetail) {
                        Log.d(TAG,"fetchMovieDetail - onNext: movieDetail: "+movieDetail);
                        resourceCallback.onResponse(Resource.success(movieDetail));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "fetchMovieDetail - onError: e: "+e.getMessage());
                        resourceCallback.onResponse(Resource.error(e.getMessage(), null));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "fetchMovieDetail - onComplete");
                    }
                });
    }
}
