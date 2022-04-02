package com.shashank.moviedb.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.shashank.moviedb.R;
import com.shashank.moviedb.data.remote.MovieApi;
import com.shashank.moviedb.data.remote.MovieRepository;
import com.shashank.moviedb.data.remote.MovieRepositoryImpl;
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

        MovieRepository repository = new MovieRepositoryImpl(movieApi);
        repository.fetchNowPlayingMovies();


    }
}