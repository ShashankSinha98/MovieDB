package com.shashank.moviedb.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.shashank.moviedb.R;
import com.shashank.moviedb.data.remote.MovieApi;
import com.shashank.moviedb.model.MovieResponse;
import com.shashank.moviedb.util.Constants;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends DaggerAppCompatActivity {

    private static final String TAG = "HomeActivity";

    @Inject
    MovieApi movieApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Map<String, String> params = new HashMap<>();
        params.put(Constants.QueryString.API_KEY, Constants.API_KEY);
        params.put(Constants.QueryString.LANGUAGE, Constants.LANGUAGE);
        params.put(Constants.QueryString.REGION, Constants.REGION);

        movieApi.fetchNowPlayingMovie(params).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "xlr8 : response: "+response);
                Log.d(TAG, "xlr8 : body: "+response.body());

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.d(TAG, "xlr8 : t: "+t);
            }
        });


    }
}