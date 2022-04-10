package com.shashank.moviedb.ui.favourites;

import android.os.Bundle;
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

import com.bumptech.glide.RequestManager;
import com.shashank.moviedb.R;
import com.shashank.moviedb.common.MovieOnClickListener;
import com.shashank.moviedb.common.ViewModelProviderFactory;
import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.data.Status;
import com.shashank.moviedb.data.MovieRepository;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.ui.trending.adapter.MovieRecyclerAdapter;
import com.shashank.moviedb.view.customview.EmptyView;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class FavouriteFragment extends DaggerFragment implements MovieOnClickListener {

    private static final String TAG = "FavouriteFragment";
    private MovieRecyclerAdapter movieRecyclerAdapter;
    @Inject public RequestManager requestManager;
    @Inject public MovieRepository movieRepository;
    @Inject public ViewModelProviderFactory providerFactory;

    private RecyclerView movieRecyclerView;
    private FavouriteViewModel favouriteViewModel;

    private EmptyView emptyView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_favourite, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieRecyclerView = view.findViewById(R.id.rv_movie);
        emptyView = view.findViewById(R.id.emptyView);

        favouriteViewModel = new ViewModelProvider(this, providerFactory).get(FavouriteViewModel.class);

        initUI();
        initObservers();
    }

    private void initObservers() {
        favouriteViewModel.getMovieIdsLiveData().observe(getViewLifecycleOwner(), new Observer<Resource<List<Long>>>() {
            @Override
            public void onChanged(Resource<List<Long>> listResource) {
                switch (listResource.getStatus()) {

                    case ERROR:
                        emptyView.updateFavourite(Status.ERROR, null, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                favouriteViewModel.fetchFavouriteMovies();
                            }
                        });
                        break;

                    case LOADING:
                        emptyView.updateFavourite(Status.LOADING, null, null);
                        break;
                }
            }
        });

        favouriteViewModel.getMoviesLiveData().observe(getViewLifecycleOwner(), new Observer<Resource<List<MovieResult>>>() {
            @Override
            public void onChanged(Resource<List<MovieResult>> listResource) {
                switch (listResource.getStatus()) {
                    case SUCCESS:
                        //movieRecyclerView.setVisibility(View.VISIBLE);
                        List<MovieResult> movies = listResource.getData();
                        emptyView.updateFavourite(Status.SUCCESS,movies.size(), null);
                        if(movies!=null && !movies.isEmpty()){
                            movieRecyclerAdapter.setMovies(movies);
                        }
                        break;
                    case ERROR:
                        emptyView.updateFavourite(Status.ERROR, null, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                favouriteViewModel.fetchFavouriteMovies();
                            }
                        });
                        break;

                    case LOADING:
                        emptyView.updateFavourite(Status.LOADING, null, null);
                        break;
                }
            }
        });

    }

    private void initUI() {
        movieRecyclerAdapter = new MovieRecyclerAdapter(requestManager, this);
        movieRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        movieRecyclerView.setAdapter(movieRecyclerAdapter);
    }

    @Override
    public void onMovieClick(Long movieId) {
        FavouriteFragmentDirections.ActionFavouriteFragmentToNavDetail action = FavouriteFragmentDirections.actionFavouriteFragmentToNavDetail();
        action.setMovieId(movieId);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(action);
    }
}
