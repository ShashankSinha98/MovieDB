package com.shashank.moviedb.ui.trending;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.shashank.moviedb.ui.trending.TrendingFragmentDirections.ActionNavTrendingToDetailFragment;
import com.shashank.moviedb.ui.trending.adapter.MovieRecyclerAdapter;
import com.shashank.moviedb.view.customview.EmptyView;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerFragment;

public class TrendingFragment extends DaggerFragment implements MovieOnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TrendingFragment";
    private MovieRecyclerAdapter movieRecyclerAdapter;
    @Inject public RequestManager requestManager;
    @Inject public MovieRepository movieRepository;
    @Inject public ViewModelProviderFactory providerFactory;

    private RecyclerView movieRecyclerView;
    private TrendingViewModel trendingViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EmptyView emptyView;


    @Inject @Named("network_status") public Boolean isNetworkAvailable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_trending, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieRecyclerView = view.findViewById(R.id.rv_movie);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        emptyView = view.findViewById(R.id.emptyView);

        trendingViewModel = new ViewModelProvider(this, providerFactory).get(TrendingViewModel.class);

        Intent intent = getActivity().getIntent();
        Uri uri = intent.getData();
        Log.d(TAG, "xlr8: uri: "+uri);

        initUI();
        initObservers();
        checkForDeepLink(getActivity().getIntent());
    }

    private void checkForDeepLink(Intent intent) {
        Uri uri = intent.getData();
        if(uri!=null) {
            int stIdx = uri.toString().indexOf("=");
            Long movieId = Long.valueOf(uri.toString().substring(stIdx+1));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onMovieClick(movieId);
                    intent.setData(null);
                }
            },250);
        }
    }


    private void initUI() {
        swipeRefreshLayout.setOnRefreshListener(this);
        movieRecyclerAdapter = new MovieRecyclerAdapter(requestManager, this);
        movieRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        movieRecyclerView.setAdapter(movieRecyclerAdapter);
    }


    private void initObservers() {
        trendingViewModel.getMoviesLiveData().observe(getViewLifecycleOwner(), new Observer<Resource<List<MovieResult>>>() {
            @Override
            public void onChanged(Resource<List<MovieResult>> listResource) {
                switch (listResource.getStatus()) {
                    case LOADING:
                        Log.d(TAG, "xlr8: initObservers : LOADING");
                        swipeRefreshLayout.setRefreshing(false);
                        movieRecyclerView.setVisibility(View.GONE);
                        emptyView.update(Status.LOADING, isNetworkAvailable,null);
                        break;

                    case SUCCESS:
                        Log.d(TAG, "xlr8: initObservers : SUCCESS");
                        swipeRefreshLayout.setRefreshing(false);
                        movieRecyclerView.setVisibility(View.VISIBLE);
                        emptyView.update(Status.SUCCESS, isNetworkAvailable,null);
                        List<MovieResult> movies = listResource.getData();
                        if(movies!=null && !movies.isEmpty()){
                            movieRecyclerAdapter.setMovies(movies);
                        }
                        break;

                    case ERROR:
                        Log.d(TAG, "xlr8: initObservers : ERROR");
                        swipeRefreshLayout.setRefreshing(false);
                        movieRecyclerView.setVisibility(View.GONE);
                        // TODO: check if error is of internet connection or something else
                        emptyView.update(Status.ERROR, isNetworkAvailable, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onRefresh();
                            }
                        });
                        String errorMessage = listResource.getMessage();
                        Log.d(TAG,"errorMessage: "+errorMessage);
                        break;
                }
            }
        });
    }


    @Override
    public void onMovieClick(Long movieId) {
        Log.d(TAG,"xlr8: movieId: "+movieId);
        ActionNavTrendingToDetailFragment action = TrendingFragmentDirections.actionNavTrendingToDetailFragment();
        action.setMovieId(movieId);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(action);
    }



    @Override
    public void onRefresh() {
        trendingViewModel.fetchTrendingMovies();
    }
}
