package com.shashank.moviedb.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.data.ResourceCallback;
import com.shashank.moviedb.data.remote.MovieRepository;
import com.shashank.moviedb.model.MovieDetail;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {

    private MovieRepository movieRepository;
    private MutableLiveData<Resource<MovieDetail>> _movieDetailResponse = new MutableLiveData<>();
    private MutableLiveData<Resource<Boolean>> _favouriteIconStatus = new MutableLiveData<>();


    @Inject
    public DetailViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void getMovieDetail(Long movieId) {
        movieRepository.fetchMovieDetail(movieId, new ResourceCallback() {
            @Override
            public void onResponse(Resource resource) {
                _movieDetailResponse.postValue(resource);
            }
        });

        validateFavouriteIcon(movieId);
    }

    private void validateFavouriteIcon(Long movieId) {
        movieRepository.isFavourite(movieId, new ResourceCallback<Boolean>() {
            @Override
            public void onResponse(Resource<Boolean> resource) {
                _favouriteIconStatus.postValue(resource);
            }
        });
    }

    public LiveData<Resource<MovieDetail>> getMovieDetailLiveData() {
        return _movieDetailResponse;
    }

    public LiveData<Resource<Boolean>> getFavouriteIconStatus() {
        return _favouriteIconStatus;
    }


    private void addMovieIdToFavourite(Long movieId) {
        movieRepository.addFavourite(movieId, new ResourceCallback<Boolean>() {
            @Override
            public void onResponse(Resource<Boolean> resource) {
                _favouriteIconStatus.postValue(resource);
            }
        });
    }

    private void deleteMovieIdFromFavourite(Long movieId) {
        movieRepository.removeFavourite(movieId, new ResourceCallback<Boolean>() {
            @Override
            public void onResponse(Resource<Boolean> resource) {
                _favouriteIconStatus.postValue(resource);
            }
        });
    }

    public void updateFavourite(Long movieId) {
        if(_favouriteIconStatus.getValue().getData()==true) {
            deleteMovieIdFromFavourite(movieId);
        } else {
            addMovieIdToFavourite(movieId);
        }
    }


}
