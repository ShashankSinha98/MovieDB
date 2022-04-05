package com.shashank.moviedb.ui.nowplaying;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.RequestManager;
import com.shashank.moviedb.R;
import com.shashank.moviedb.common.MovieOnClickListener;
import com.shashank.moviedb.common.ViewModelProviderFactory;
import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.data.Status;
import com.shashank.moviedb.data.remote.MovieRepository;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.ui.trending.adapter.MovieRecyclerAdapter;
import com.shashank.moviedb.view.customview.EmptyView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerFragment;

public class NowPlayingFragment extends DaggerFragment implements MovieOnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "NowPlayingFragment";
    private MovieRecyclerAdapter movieRecyclerAdapter;
    @Inject public RequestManager requestManager;
    @Inject public MovieRepository movieRepository;
    @Inject public ViewModelProviderFactory providerFactory;

    private RecyclerView movieRecyclerView;
    private NowPlayingViewModel nowPlayingViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EmptyView emptyView;

    @Inject @Named("network_status") public Boolean isNetworkAvailable;

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
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        emptyView = view.findViewById(R.id.emptyView);

        nowPlayingViewModel = new ViewModelProvider(this, providerFactory).get(NowPlayingViewModel.class);

        initUI();
        initObservers();
    }


    private void initUI() {
        swipeRefreshLayout.setOnRefreshListener(this);
        movieRecyclerAdapter = new MovieRecyclerAdapter(requestManager, this);
        movieRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        movieRecyclerView.setAdapter(movieRecyclerAdapter);
    }


    private void initObservers() {
        nowPlayingViewModel.getMoviesLiveData().observe(getViewLifecycleOwner(), new Observer<Resource<List<MovieResult>>>() {
            @Override
            public void onChanged(Resource<List<MovieResult>> listResource) {
                switch (listResource.getStatus()) {
                    case SUCCESS:
                        Log.d(TAG, "xlr8: initObservers : SUCCESS");
                        swipeRefreshLayout.setRefreshing(false);
                        movieRecyclerView.setVisibility(View.VISIBLE);
                        emptyView.update(Status.SUCCESS,isNetworkAvailable,null);
                        List<MovieResult> movies = listResource.getData();
                        if(movies!=null && !movies.isEmpty()){
                            movieRecyclerAdapter.setMovies(movies);
                        }
                        break;

                    case ERROR:
                        Log.d(TAG, "xlr8: initObservers : ERROR");
                        swipeRefreshLayout.setRefreshing(false);
                        movieRecyclerView.setVisibility(View.GONE);
                        emptyView.update(Status.ERROR, isNetworkAvailable, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onRefresh();
                            }
                        });
                        String errorMessage = listResource.getMessage();
                        Log.d(TAG,"errorMessage: "+errorMessage);
                        break;

                    case LOADING:
                        Log.d(TAG, "xlr8: initObservers : LOADING");
                        swipeRefreshLayout.setRefreshing(false);
                        movieRecyclerView.setVisibility(View.GONE);
                        emptyView.update(Status.LOADING, isNetworkAvailable, null);
                        break;
                }
            }
        });
    }

    @Override
    public void onMovieClick(Long movieId) {
        Log.d(TAG,"xlr8: movieId: "+movieId);
        NowPlayingFragmentDirections.ActionNavNowPlayingToDetailFragment action = NowPlayingFragmentDirections.actionNavNowPlayingToDetailFragment();
        action.setMovieId(movieId);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(action);

    }

    @Override
    public void onRefresh() {
        nowPlayingViewModel.fetchNowPlayingMovies();
    }
}
