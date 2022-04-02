package com.shashank.moviedb.data.remote;


/* Note: Currently we will make all network request on main thread
*  @TODO: Add suspend when using coroutines
* */

import com.shashank.moviedb.model.MovieResponse;

import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface MovieApi {

    @GET("movie/now_playing")
    Observable<MovieResponse> fetchNowPlayingMovie(@QueryMap Map<String, String> params);


    @GET("trending/movie/{time_window}")
    Observable<MovieResponse> fetchTrendingMovie(@Path("time_window") String time,
                                                 @QueryMap Map<String, String> params);
}
