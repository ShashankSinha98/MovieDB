package com.shashank.moviedb.data.remote;

import static com.shashank.moviedb.util.Constants.TRENDING_TIME_WINDOW;

import android.util.Log;

import androidx.annotation.NonNull;

import com.shashank.moviedb.data.ResourceCallback;
import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.data.local.MovieDao;
import com.shashank.moviedb.data.local.MovieDatabase;
import com.shashank.moviedb.model.MovieDetail;
import com.shashank.moviedb.model.MovieResponse;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.util.Constants;
import com.shashank.moviedb.util.Constants.QueryParams;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieRepositoryImpl implements MovieRepository {

    private static final String TAG = "MovieRepositoryImpl";

    private MovieApi movieApi;
    private MovieDao movieDao;

    @Inject
    public MovieRepositoryImpl(MovieApi movieApi, MovieDao movieDao) {
        this.movieApi = movieApi;
        this.movieDao = movieDao;
    }


    @Override
    public void fetchNowPlayingMovies(ResourceCallback resourceCallback) {
        resourceCallback.onResponse(Resource.loading("Loading Now Playing Movies"));

        movieApi.fetchNowPlayingMovie(QueryParams.PARAMS_NOW_PLAYING_MOVIE_API)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(Constants.DUMMY_NETWORK_DELAY, TimeUnit.MILLISECONDS)
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
        resourceCallback.onResponse(Resource.loading("Loading Trending Movies"));

        movieApi.fetchTrendingMovie(TRENDING_TIME_WINDOW, QueryParams.PARAMS_TRENDING_MOVIE_API)
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .delay(Constants.DUMMY_NETWORK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<MovieResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG,"fetchTrendingMovies - onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull MovieResponse movieResponse) {
                        Log.d(TAG,"fetchTrendingMovies - onNext: movieResponse: "+movieResponse);
                        //resourceCallback.onResponse(Resource.success(movieResponse));
                        processMovieResponse(movieResponse, resourceCallback);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "xlr8: fetchTrendingMovies - onError: e: "+e.getMessage());
                        //resourceCallback.onResponse(Resource.error(e.getMessage(), null));
                        checkDataInDatabase(resourceCallback);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "xlr8: fetchTrendingMovies - onComplete");
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
                //.observeOn(AndroidSchedulers.mainThread())
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



    // Save remote data in room db, read data from db and then pass it to callback
    private void processMovieResponse(MovieResponse movieResponse, ResourceCallback callback) {
        Log.d(TAG, "xlr8: processMovieResponse called: movieResponse: "+movieResponse);

        if(movieResponse==null || movieResponse.getResults()==null || movieResponse.getResults().isEmpty()) {
            callback.onResponse(Resource.error("NULL/EMPTY API RESPONSE", null));
            return;
        }

        List<MovieResult> movies = movieResponse.getResults();

        // Insert movies data in database
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                movieDao.insertMovieResults(movies.toArray(new MovieResult[movies.size()]));
            }
        }).subscribeOn(Schedulers.io())
            .subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "xlr8: processMovieResponse: onSubscribe");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "xlr8: processMovieResponse: onComplete");
                // Movies inserted in DB Successfully
                // Fetch movies data from DB and pass it to callback
                checkDataInDatabase(callback);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "xlr8: processMovieResponse: e: "+e.getMessage());
                callback.onResponse(Resource.error("Error inserting remote data in DB", null));
            }
        });
    }


    private void checkDataInDatabase(ResourceCallback callback) {
        Log.d(TAG, "xlr8 : checkDataInDatabase called");
        // Fetch movies data from DB
        movieDao.getMovieResults().subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<MovieResult>>() {
                    @Override
                    public void accept(List<MovieResult> movieResults) throws Exception {
                        Log.d(TAG, "xlr8: processMovieResponse : accept: movieResults: "+movieResults);
                        if(movieResults==null || movieResults.size()==0)
                            callback.onResponse(Resource.error("Error Fetching Data from DB / DB is Empty", null));
                        else callback.onResponse(Resource.success(movieResults));
                    }
                });
    }
}
