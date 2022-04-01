package com.shashank.moviedb.data.remote;


/* Note: Currently we will make all network request on main thread
*  @TODO: Add suspend when using coroutines
* */

import com.shashank.moviedb.model.MovieResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface MovieApi {

    @GET("/3/movie/now_playing")
    Call<MovieResponse> fetchNowPlayingMovie(@QueryMap Map<String, String> params);
}
