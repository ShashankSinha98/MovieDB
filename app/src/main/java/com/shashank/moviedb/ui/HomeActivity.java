package com.shashank.moviedb.ui;

import android.os.Bundle;
import android.util.Log;

import com.shashank.moviedb.R;
import com.shashank.moviedb.data.ResourceCallback;
import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.data.Status;
import com.shashank.moviedb.data.remote.MovieApi;
import com.shashank.moviedb.data.remote.MovieRepository;
import com.shashank.moviedb.data.remote.MovieRepositoryImpl;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class HomeActivity extends DaggerAppCompatActivity {

    private static final String TAG = "HomeActivity";

    @Inject
    public MovieRepository movieRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieRepository.fetchTrendingMovies(new ResourceCallback() {
            @Override
            public void onResponse(Resource resource) {
                Log.d(TAG, "xlr8: Resource: "+resource);
            }
        });

    }
}