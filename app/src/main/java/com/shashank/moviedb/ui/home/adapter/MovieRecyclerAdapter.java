package com.shashank.moviedb.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.shashank.moviedb.R;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.ui.home.viewholder.MovieViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int MOVIE_TYPE = 1;
    private static final int EXHAUSTED_TYPE = 2;
    private RequestManager requestManager;

    @Inject
    public MovieRecyclerAdapter(RequestManager requestManager) {
        this.requestManager = requestManager;
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
                return new MovieViewHolder(view, requestManager);
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
        this.mMovies = movies;
        notifyDataSetChanged();
    }
}
