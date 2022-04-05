package com.shashank.moviedb.ui.detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.data.ResourceCallback;
import com.shashank.moviedb.data.Status;
import com.shashank.moviedb.data.remote.MovieRepository;
import com.shashank.moviedb.model.MovieDetail;
import com.shashank.moviedb.model.MovieResult;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {

    private static final String TAG = "DetailViewModel";

    private MovieRepository movieRepository;
    private MutableLiveData<Resource<MovieDetail>> _movieDetailResponse = new MutableLiveData<>();
    private MutableLiveData<Resource<List<MovieResult>>> _movieResultResponse = new MutableLiveData<>();
    private MutableLiveData<Resource<Boolean>> _favouriteIconStatus = new MutableLiveData<>();


    @Inject
    public DetailViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void getMovieDetail(Long movieId) {
        movieRepository.fetchMovieDetail(movieId, new ResourceCallback() {
            @Override
            public void onResponse(Resource resource) {
                if(resource.getStatus()== Status.SUCCESS)
                    _movieDetailResponse.postValue(resource);
                else
                    checkMovieDataInMovieTable(movieId);
            }
        });

        validateFavouriteIcon(movieId);
    }

    private void checkMovieDataInMovieTable(Long movieId) {
        Log.d(TAG, "xlr8: checkMovieDataInMovieTable called, movieId: "+movieId);
        List<Long> movieIds = new ArrayList<>();
        movieIds.add(movieId);

        movieRepository.checkMovieDataInDatabaseForIds(movieIds, new ResourceCallback<List<MovieResult>>() {
            @Override
            public void onResponse(Resource<List<MovieResult>> resource) {
                _movieResultResponse.postValue(resource);
            }
        });
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

    public LiveData<Resource<List<MovieResult>>> getMovieResultLiveData() {
        return _movieResultResponse;
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
