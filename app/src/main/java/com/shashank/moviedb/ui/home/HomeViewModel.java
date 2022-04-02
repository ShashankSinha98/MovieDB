package com.shashank.moviedb.ui.home;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.data.ResourceCallback;
import com.shashank.moviedb.data.Status;
import com.shashank.moviedb.data.remote.MovieRepository;
import com.shashank.moviedb.model.MovieResponse;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.util.Constants;

import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";

    private MovieRepository movieRepository;
    private MutableLiveData<List<MovieResult>> _movies = new MutableLiveData<>();

    @Inject
    public HomeViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;

        fetchTrendingMovies();
    }

    private void fetchTrendingMovies() {
        movieRepository.fetchTrendingMovies(new ResourceCallback() {
            @Override
            public void onResponse(Resource resource) {
                if(resource.getStatus() == Status.SUCCESS) {
                    Log.d(TAG,"xlr8: fetchTrendingMovies: SUCCESS: ");
                    List<MovieResult> movies = ((MovieResponse)resource.getData()).getResults();
                    movies.add(getExhaustedEntry());
                    _movies.postValue(movies);
                } else if(resource.getStatus()==Status.LOADING) {
                    String message = (String) resource.getData();
                    Log.d(TAG,"xlr8: fetchTrendingMovies: msg: "+message);
                }
            }
        });
    }

    private MovieResult getExhaustedEntry() {
        MovieResult exhaustedDummyMovie = new MovieResult();
        exhaustedDummyMovie.setTitle(Constants.CONST_EXHAUSTED);
        return exhaustedDummyMovie;
    }

    public LiveData<List<MovieResult>> getMoviesLiveData() {
        return _movies;
    }



}
