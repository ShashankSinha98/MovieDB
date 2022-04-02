package com.shashank.moviedb.data.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.model.MovieResponse;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.util.Constants;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieRepositoryImpl implements MovieRepository {

    private static final String TAG = "MovieRepositoryImpl";

    @NonNull private MovieApi movieApi;

    public MovieRepositoryImpl(@NonNull MovieApi movieApi) {
        this.movieApi = movieApi;
    }

    @Override
    public Resource<List<MovieResult>> fetchNowPlayingMovies() {

        movieApi.fetchNowPlayingMovie(Constants.QueryParams.PARAMS_NOW_PLAYING_MOVIE_API)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull MovieResponse movieResponse) {
                        Log.d(TAG,"xlr8: movieResponse: "+movieResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return null;
    }
}
