package com.shashank.moviedb.ui.nowplaying;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shashank.moviedb.R;
import com.shashank.moviedb.common.ViewModelProviderFactory;
import com.shashank.moviedb.data.remote.MovieRepository;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.ui.trending.TrendingViewModel;
import com.shashank.moviedb.ui.trending.adapter.MovieRecyclerAdapter;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class NowPlayingFragment extends DaggerFragment {

    @Inject public MovieRecyclerAdapter movieRecyclerAdapter;
    @Inject public MovieRepository movieRepository;
    @Inject public ViewModelProviderFactory providerFactory;

    private RecyclerView movieRecyclerView;
    private NowPlayingViewModel nowPlayingViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_now_playing, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieRecyclerView = view.findViewById(R.id.rv_movie);

        nowPlayingViewModel = new ViewModelProvider(this, providerFactory).get(NowPlayingViewModel.class);

        initUI();
        initObservers();
    }


    private void initUI() {
        movieRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        movieRecyclerView.setAdapter(movieRecyclerAdapter);
    }


    private void initObservers() {
        nowPlayingViewModel.getMoviesLiveData().observe(getViewLifecycleOwner(), new Observer<List<MovieResult>>() {
            @Override
            public void onChanged(List<MovieResult> movieResults) {
                movieRecyclerAdapter.setMovies(movieResults);
            }
        });
    }

}
