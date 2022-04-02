package com.shashank.moviedb.data.remote;

import androidx.annotation.NonNull;

import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.model.MovieResult;

import java.util.List;

public interface MovieRepository {

    Resource<List<MovieResult>> fetchNowPlayingMovies();
}
