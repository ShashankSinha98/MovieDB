package com.shashank.moviedb.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.shashank.moviedb.R;
import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.data.ResourceCallback;
import com.shashank.moviedb.data.Status;
import com.shashank.moviedb.data.remote.MovieRepository;
import com.shashank.moviedb.model.MovieResponse;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.ui.home.adapter.MovieRecyclerAdapter;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class HomeFragment extends DaggerFragment {

    @Inject public MovieRecyclerAdapter movieRecyclerAdapter;
    @Inject public RecyclerView.LayoutManager gridLayoutManager;
    @Inject public MovieRepository movieRepository;
    private RecyclerView movieRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieRecyclerView = view.findViewById(R.id.rv_movie);

        initUI();
        movieRepository.fetchTrendingMovies(new ResourceCallback() {
            @Override
            public void onResponse(Resource resource) {
                if(resource.getStatus() == Status.SUCCESS) {
                    List<MovieResult> movies = ((MovieResponse)resource.getData()).getResults();
                    movieRecyclerAdapter.setMovies(movies);
                }
            }
        });
    }

    private void initUI() {
        movieRecyclerView.setLayoutManager(gridLayoutManager);
        movieRecyclerView.setAdapter(movieRecyclerAdapter);
    }


}
