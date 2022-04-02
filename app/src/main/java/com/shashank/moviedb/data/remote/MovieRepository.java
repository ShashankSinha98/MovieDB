package com.shashank.moviedb.data.remote;

import com.shashank.moviedb.data.ResourceCallback;
import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.model.MovieResult;

import java.util.List;

public interface MovieRepository {

    void fetchNowPlayingMovies(ResourceCallback resourceCallback);

    void fetchTrendingMovies(ResourceCallback resourceCallback);

    void fetchMovieDetail(Long movieId, ResourceCallback resourceCallback);
}
