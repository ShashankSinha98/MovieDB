package com.shashank.moviedb.ui.trending;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.data.ResourceCallback;
import com.shashank.moviedb.data.MovieRepository;
import com.shashank.moviedb.model.MovieResult;

import java.util.List;

import javax.inject.Inject;

public class TrendingViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";

    private MovieRepository movieRepository;
    private MutableLiveData<Resource<List<MovieResult>>> _movies = new MutableLiveData<>();

    @Inject
    public TrendingViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;

        fetchTrendingMovies();
    }

    public void fetchTrendingMovies() {
        Log.d(TAG, "xlr8: fetchTrendingMovies called");
        movieRepository.fetchTrendingMovies(new ResourceCallback() {
            @Override
            public void onResponse(Resource resource) {
                _movies.postValue(resource);
            }
        });
    }



    public LiveData<Resource<List<MovieResult>>> getMoviesLiveData() {
        return _movies;
    }



}
