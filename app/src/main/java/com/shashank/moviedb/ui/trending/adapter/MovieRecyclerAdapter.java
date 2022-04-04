package com.shashank.moviedb.ui.trending.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.shashank.moviedb.R;
import com.shashank.moviedb.common.MovieOnClickListener;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.ui.trending.viewholder.MovieViewHolder;
import com.shashank.moviedb.util.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MOVIE_TYPE = 1;
    private static final int EXHAUSTED_TYPE = 2;
    private RequestManager requestManager;
    private MovieOnClickListener movieOnClickListener;

    @Inject
    public MovieRecyclerAdapter(RequestManager requestManager, MovieOnClickListener movieOnClickListener) {
        this.requestManager = requestManager;
        this.movieOnClickListener = movieOnClickListener;
    }

    private List<MovieResult> mMovies = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = null;

        switch (viewType) {

            case EXHAUSTED_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_exhausted, parent, false);
                return new SearchExhaustedViewHolder(view);

            case MOVIE_TYPE:

            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_view_layout, parent, false);
                return new MovieViewHolder(view, requestManager, movieOnClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        if(viewType == MOVIE_TYPE) {
            ((MovieViewHolder)holder).onBind(mMovies.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(!mMovies.isEmpty() && !mMovies.get(position).getTitle().equals("EXHAUSTED")) {
            return MOVIE_TYPE;
        } else if(!mMovies.isEmpty() && mMovies.get(position).getTitle().equals("EXHAUSTED")){
            return EXHAUSTED_TYPE;
        }

        return MOVIE_TYPE;
    }


    public void setMovies(List<MovieResult> movies) {
        //if(!movies.isEmpty() && !movies.get(movies.size()-1).getTitle().equals(Constants.CONST_EXHAUSTED)) {movies.add(getExhaustedEntry());}
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    private MovieResult getExhaustedEntry() {
        MovieResult exhaustedDummyMovie = new MovieResult();
        exhaustedDummyMovie.setTitle(Constants.CONST_EXHAUSTED);
        return exhaustedDummyMovie;
    }
}
