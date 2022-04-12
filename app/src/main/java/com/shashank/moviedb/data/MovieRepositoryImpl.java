package com.shashank.moviedb.data;

import static com.shashank.moviedb.util.Constants.*;
import static com.shashank.moviedb.util.Constants.TRENDING_TIME_WINDOW;

import android.util.Log;

import androidx.annotation.NonNull;

import com.shashank.moviedb.data.local.MovieDao;
import com.shashank.moviedb.data.local.entity.FavouriteMovieIdsEntity;
import com.shashank.moviedb.data.remote.MovieApi;
import com.shashank.moviedb.model.MovieDetail;
import com.shashank.moviedb.model.MovieResponse;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.data.local.entity.NowPlayingMovieIdsEntity;
import com.shashank.moviedb.data.local.entity.TrendingMovieIdsEntity;
import com.shashank.moviedb.util.Constants.QueryParams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

// Data is sent to ViewModel via callback (@ResourceCallback)
// Following Single source of truth principle

public class MovieRepositoryImpl implements MovieRepository {

    private static final String TAG = "MovieRepositoryImpl";

    private MovieApi movieApi;
    private MovieDao movieDao;

    @Inject
    public MovieRepositoryImpl(MovieApi movieApi, MovieDao movieDao) {
        this.movieApi = movieApi;
        this.movieDao = movieDao;
    }


    /**   NOW PLAYING MOVIES  **/
    @Override
    public void fetchNowPlayingMovies(ResourceCallback resourceCallback) {
        resourceCallback.onResponse(Resource.loading("Loading Now Playing Movies"));

        movieApi.fetchNowPlayingMovie(QueryParams.PARAMS_NOW_PLAYING_MOVIE_API)
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .delay(DUMMY_NETWORK_DELAY, TimeUnit.MILLISECONDS)
                .subscribe(new Observer<MovieResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG,"fetchNowPlayingMovies - onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull MovieResponse movieResponse) {
                        Log.d(TAG,"fetchNowPlayingMovies - onNext: movieResponse: "+movieResponse);
                        try {
                            processMovieResponse(movieResponse, resourceCallback, STATUS_NOW_PLAYING);
                        } catch (Exception e) {
                            resourceCallback.onResponse(Resource.error(e.getMessage(), null));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "fetchNowPlayingMovies - onError: e: "+e.getMessage());
                        checkMovieDataInDatabase(resourceCallback, STATUS_NOW_PLAYING);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "fetchNowPlayingMovies - onComplete");
                    }
                });
    }


    /**   TRENDING MOVIES  **/
    @Override
    public void fetchTrendingMovies(ResourceCallback resourceCallback) {

        // 1. Set current request status as Loading...
        resourceCallback.onResponse(Resource.loading("Loading Trending Movies"));


        // 2. Fetch data from API
        movieApi.fetchTrendingMovie(TRENDING_TIME_WINDOW, QueryParams.PARAMS_TRENDING_MOVIE_API)
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .delay(DUMMY_NETWORK_DELAY, TimeUnit.MILLISECONDS) // TODO: custom delay added - remove from production
                .subscribe(new Observer<MovieResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG,"fetchTrendingMovies - onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull MovieResponse movieResponse) {
                        Log.d(TAG,"fetchTrendingMovies - onNext: movieResponse: "+movieResponse);
                        try {
                            processMovieResponse(movieResponse, resourceCallback, STATUS_TRENDING);
                        } catch (Exception e) {
                            resourceCallback.onResponse(Resource.error(e.getMessage(), null));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "xlr8: fetchTrendingMovies - onError: e: "+e.getMessage());
                        checkMovieDataInDatabase(resourceCallback, STATUS_TRENDING);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "xlr8: fetchTrendingMovies - onComplete");
                    }
                });
    }


    @Override
    public void fetchMovieDetail(Long movieId, ResourceCallback resourceCallback) {
        Log.d(TAG, "xlr8: fetchMovieDetail: movieId: "+movieId);
        if(movieId==null || movieId<=0L) {
            resourceCallback.onResponse(Resource.error(INVALID_MOVIE_ID_ERROR_MSG,null));
            return;
        }

        movieApi.fetchMovieDetail(movieId, QueryParams.PARAMS_MOVIE_DETAIL_API)
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieDetail>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TAG,"xlr8: fetchMovieDetail - onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull MovieDetail movieDetail) {
                        Log.d(TAG,"xlr8: fetchMovieDetail - onNext: movieDetail: "+movieDetail);
                        try {
                            processMovieDetailResponse(movieDetail, resourceCallback);
                        } catch (Exception e) {
                            resourceCallback.onResponse(Resource.error(e.getMessage(), null));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "xlr8: fetchMovieDetail - onError: e: "+e.getMessage());
                        checkMovieDetailDataInDatabase(movieId, resourceCallback);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "xlr8: fetchMovieDetail - onComplete");
                    }
                });
    }

    @Override
    public void isFavourite(long movieId, ResourceCallback<Boolean> resourceCallback) {
        resourceCallback.onResponse(Resource.loading("Querying favourite status for movieId: "+movieId));

        movieDao.getFavouriteCountForMovieId(movieId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer count) throws Exception {
                        resourceCallback.onResponse(Resource.success((count>0)));
                    }
                });
    }


    @Override
    public void addFavourite(long movieId, ResourceCallback<Boolean> resourceCallback) {
        resourceCallback.onResponse(Resource.loading("Adding Movie to Favourite, movieId: "+movieId));

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                movieDao.insertFavouriteMovieId(new FavouriteMovieIdsEntity(movieId));
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        resourceCallback.onResponse(Resource.success(true));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        resourceCallback.onResponse(Resource.error("Error adding movieId: "+movieId+" to favourite", null));
                    }
                });

    }

    @Override
    public void removeFavourite(long movieId, ResourceCallback<Boolean> resourceCallback) {
        resourceCallback.onResponse(Resource.loading("Removing Movie from Favourite, movieId: "+movieId));

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                movieDao.deleteFavouriteMovieId(new FavouriteMovieIdsEntity(movieId));
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        resourceCallback.onResponse(Resource.success(false));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        resourceCallback.onResponse(Resource.error("Error removing movieId: "+movieId+" from favourite", null));
                    }
                });

    }

    @Override
    public void getFavouriteMovieIds(ResourceCallback<List<Long>> resourceCallback) {
        resourceCallback.onResponse(Resource.loading("Querying Favourite movie ids from db"));

        movieDao.getFavouriteMovieIdsEntities()
                .subscribeOn(Schedulers.computation())
                .subscribe(new Consumer<List<FavouriteMovieIdsEntity>>() {
                    @Override
                    public void accept(List<FavouriteMovieIdsEntity> favouriteMovieIdsEntities) throws Exception {
                        if(favouriteMovieIdsEntities==null) {
                            resourceCallback.onResponse(Resource.error("Error favourite query response", null));;
                        } else {
                            List<Long> favouriteIds = new ArrayList<>();
                            for(FavouriteMovieIdsEntity favouriteMovieIdsEntity: favouriteMovieIdsEntities) {
                                favouriteIds.add(favouriteMovieIdsEntity.getMovieId());
                            }
                            resourceCallback.onResponse(Resource.success(favouriteIds));
                        }
                    }
                });
    }

    @Override
    public void checkMovieDataInDatabaseForIds(List<Long> movieIds, ResourceCallback<List<MovieResult>> callback) {
        Log.d(TAG, "xlr8: checkMovieDataInDatabaseForIds, movieIds: "+movieIds);
        // Fetch movies data from DB
        movieDao.getMovieResultsForMovieIds(movieIds).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<MovieResult>>() {
                    @Override
                    public void accept(List<MovieResult> movieResults) throws Exception {
                        Log.d(TAG, "xlr8: processMovieResponse : accept: movieResults: "+movieResults);
                        if(movieResults==null)
                            callback.onResponse(Resource.error("Error Fetching Data from DB / DB is Empty", null));
                        else callback.onResponse(Resource.success(movieResults));
                    }
                });
    }



    // Movie Detail

    @Override
    public void addMovieDetail(MovieDetail movieDetail, ResourceCallback<Boolean> resourceCallback) {

    }

    @Override
    public void getMovieDetail(long movieId, ResourceCallback<MovieDetail> resourceCallback) {

    }


    /**********************************************     PRIVATE  HELPER   FUNCTIONS     *****************************************************************************/

    // Save remote data in room db, read data from db and then pass it to callback
    private void processMovieResponse(MovieResponse movieResponse, ResourceCallback callback, int movieStatus) throws Exception {
        Log.d(TAG, "xlr8: processMovieResponse called: movieResponse: "+movieResponse);

        if(movieResponse==null || movieResponse.getResults()==null || movieResponse.getResults().isEmpty()) {
            callback.onResponse(Resource.error("NULL/EMPTY API RESPONSE", null));
            return;
        }

        List<MovieResult> movies = movieResponse.getResults();

        // step 1- Insert movies data in database
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                // Step 1.1- Insert movie ids into respective Entity
                insertMovieIdsIntoRespectiveEntity(movies, movieStatus);

                // Step 1.2- Insert MovieResult in main movie table
                movieDao.insertMovieResults(movies.toArray(new MovieResult[movies.size()]));
            }
        }).subscribeOn(Schedulers.computation())
            .subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "xlr8: processMovieResponse: onSubscribe");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "xlr8: processMovieResponse: onComplete");
                // Movies inserted in DB Successfully

                // Step 2- Fetch movies data from DB and pass it to UI
                checkMovieDataInDatabase(callback, movieStatus);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "xlr8: processMovieResponse: e: "+e.getMessage());
                callback.onResponse(Resource.error("Error inserting remote data in DB", null));
            }
        });
    }


    private void insertMovieIdsIntoRespectiveEntity(List<MovieResult> movies, int movieStatus) {
        if(movieStatus== STATUS_NOW_PLAYING) {
            List<NowPlayingMovieIdsEntity> nowPlayingMovieIds = new ArrayList<>();
            for(MovieResult movie: movies) {
                nowPlayingMovieIds.add(new NowPlayingMovieIdsEntity(movie.getId()));
            }
            movieDao.insertNowPlayingMovieIds(nowPlayingMovieIds.toArray(new NowPlayingMovieIdsEntity[nowPlayingMovieIds.size()]));

        } else if(movieStatus== STATUS_TRENDING) {
            List<TrendingMovieIdsEntity> trendingMovieIds = new ArrayList<>();
            for(MovieResult movie: movies) {
                trendingMovieIds.add(new TrendingMovieIdsEntity(movie.getId()));
            }
            movieDao.insertTrendingMovieIds(trendingMovieIds.toArray(new TrendingMovieIdsEntity[trendingMovieIds.size()]));
        }
    }

    // Check data in DB, if present - pass via callback
    private void checkMovieDataInDatabase(ResourceCallback callback, int movieStatus) {
        Log.d(TAG, "xlr8 : checkDataInDatabase called");

        // Step 2.1 - Get list of movie ids from respective movie status table
        if(movieStatus==STATUS_TRENDING) {
            movieDao.getTrendingMovieIds().subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<TrendingMovieIdsEntity>>() {
                        @Override
                        public void accept(List<TrendingMovieIdsEntity> trendingMovieIdsEntities) throws Exception {
                            if(trendingMovieIdsEntities!=null && !trendingMovieIdsEntities.isEmpty()) {
                                List<Long> trendingMovieIds = new ArrayList<>();
                                for(TrendingMovieIdsEntity trendingMovieIdsEntity: trendingMovieIdsEntities) {
                                    trendingMovieIds.add(trendingMovieIdsEntity.getMovieId());
                                }

                                // Step 2.2- fetch movie of these ids only
                                checkMovieDataInDatabaseForIds(trendingMovieIds, callback);
                            } else {
                                callback.onResponse(Resource.error("Error/Empty response while querying DB", null));
                            }
                        }
                    });

        }
        // Step 2.1 - Get list of movie ids from respective movie status table
        else if(movieStatus==STATUS_NOW_PLAYING) {
            movieDao.getNowPlayingMovieIds().subscribeOn(Schedulers.io())
                    .subscribe(new Consumer<List<NowPlayingMovieIdsEntity>>() {
                        @Override
                        public void accept(List<NowPlayingMovieIdsEntity> nowPlayingMovieIdsEntities) throws Exception {
                            if(nowPlayingMovieIdsEntities!=null && !nowPlayingMovieIdsEntities.isEmpty()) {
                                List<Long> nowPlayingMovieIds = new ArrayList<>();
                                for(NowPlayingMovieIdsEntity nowPlayingMovieIdsEntity: nowPlayingMovieIdsEntities) {
                                    nowPlayingMovieIds.add(nowPlayingMovieIdsEntity.getMovieId());
                                }

                                // Step 2.2- fetch movie of these ids only
                                checkMovieDataInDatabaseForIds(nowPlayingMovieIds, callback);
                            } else {
                                callback.onResponse(Resource.error("Error/Empty response while querying DB", null));
                            }
                        }
                    });
        }

    }

    private void processMovieDetailResponse(MovieDetail movieDetail, ResourceCallback callback) throws Exception {
        Log.d(TAG, "xlr8: processMovieDetailResponse called: movieDetail: "+movieDetail);

        if(movieDetail==null) {
            callback.onResponse(Resource.error("Null Movie Detail response", null));
            return;
        }

        // Step 1- Insert into Movie Detail Table
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                movieDao.insertMovieDetail(movieDetail);
            }
        }).subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "xlr8:processMovieDetailResponse onComplete");
                        // Movie Detail inserted in DB

                        // Step 2- Fetch data from DB
                        checkMovieDetailDataInDatabase(movieDetail.getId(), callback);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "xlr8:processMovieDetailResponse onError: e: "+e.getMessage());
                        callback.onResponse(Resource.error("Error inserting Movie Detail data in DB", null));
                    }
                });
    }


    private void checkMovieDetailDataInDatabase(Long movieId, ResourceCallback callback) {
        Log.d(TAG, "xlr8: checkMovieDetailDataInDatabase called, movieId: "+movieId);
        movieDao.getMovieDetailForMovieId(movieId)
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<MovieDetail>>() {
                    @Override
                    public void accept(List<MovieDetail> movieDetails) throws Exception {
                        Log.d(TAG, "xlr8: checkMovieDetailDataInDatabase accept: movieDetail: "+movieDetails);
                        if(movieDetails!=null && movieDetails.size()!=0) {
                            callback.onResponse(Resource.success(movieDetails.get(0)));
                        } else {
                            callback.onResponse(Resource.error("Null Movie Detail Query Response", null));
                        }
                    }
                });
    }
}
