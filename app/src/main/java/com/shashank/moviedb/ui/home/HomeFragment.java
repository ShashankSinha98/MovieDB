package com.shashank.moviedb.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.shashank.moviedb.R;
import com.shashank.moviedb.common.ViewModelProviderFactory;
import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.data.ResourceCallback;
import com.shashank.moviedb.data.Status;
import com.shashank.moviedb.data.remote.MovieRepository;
import com.shashank.moviedb.model.MovieResponse;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.ui.home.adapter.MovieRecyclerAdapter;
import com.shashank.moviedb.util.Constants;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class HomeFragment extends DaggerFragment {

    @Inject public MovieRecyclerAdapter movieRecyclerAdapter;
    @Inject public RecyclerView.LayoutManager gridLayoutManager;
    @Inject public MovieRepository movieRepository;
    @Inject public ViewModelProviderFactory providerFactory;

    private RecyclerView movieRecyclerView;
    private HomeViewModel homeViewModel;

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

        homeViewModel = new ViewModelProvider(this, providerFactory).get(HomeViewModel.class);

        initUI();
        initObservers();
    }


    private void initUI() {
        movieRecyclerView.setLayoutManager(gridLayoutManager);
        movieRecyclerView.setAdapter(movieRecyclerAdapter);
    }


    private void initObservers() {
        homeViewModel.getMoviesLiveData().observe(getViewLifecycleOwner(), new Observer<List<MovieResult>>() {
            @Override
            public void onChanged(List<MovieResult> movieResults) {
                movieRecyclerAdapter.setMovies(movieResults);
            }
        });
    }




}
