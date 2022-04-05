package com.shashank.moviedb.ui.favourites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.data.ResourceCallback;
import com.shashank.moviedb.data.Status;
import com.shashank.moviedb.data.remote.MovieRepository;
import com.shashank.moviedb.model.MovieResult;

import java.util.List;

import javax.inject.Inject;

public class FavouriteViewModel extends ViewModel  {

    private static final String TAG = "FavouriteViewModel";

    private MovieRepository movieRepository;
    private MutableLiveData<Resource<List<MovieResult>>> _movies = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Long>>> _movieIds = new MutableLiveData<>();

    @Inject
    public FavouriteViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        fetchFavouriteMovies();
    }

    public void fetchFavouriteMovies() {

        movieRepository.getFavouriteMovieIds(new ResourceCallback<List<Long>>() {
            @Override
            public void onResponse(Resource<List<Long>> resource) {
                if(resource.getStatus()== Status.SUCCESS) {
                    fetchMovieResponsesForGivenIds(resource.getData());
                } else {
                    _movieIds.postValue(resource);
                }
            }
        });
    }

    private void fetchMovieResponsesForGivenIds(List<Long> movieIds) {
        movieRepository.checkMovieDataInDatabaseForIds(movieIds, new ResourceCallback<List<MovieResult>>() {
            @Override
            public void onResponse(Resource<List<MovieResult>> resource) {
                _movies.postValue(resource);
            }
        });
    }

    public LiveData<Resource<List<Long>>> getMovieIdsLiveData() {
        return _movieIds;
    }

    public LiveData<Resource<List<MovieResult>>> getMoviesLiveData() {
        return _movies;
    }
}
