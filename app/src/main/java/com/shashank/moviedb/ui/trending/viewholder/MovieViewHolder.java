package com.shashank.moviedb.ui.trending.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.shashank.moviedb.R;
import com.shashank.moviedb.common.MovieOnClickListener;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.util.Constants;

public class MovieViewHolder extends RecyclerView.ViewHolder {


    private RequestManager requestManager;

    private AppCompatImageView movieImage;
    private TextView movieTitle;
    private TextView movieVoteAverage;
    private MovieOnClickListener movieOnClickListener;
    private CardView movieCard;

    public MovieViewHolder(@NonNull View itemView, RequestManager requestManager, MovieOnClickListener movieOnClickListener) {
        super(itemView);
        this.requestManager = requestManager;
        this.movieOnClickListener = movieOnClickListener;
        movieImage = itemView.findViewById(R.id.iv_movie);
        movieTitle = itemView.findViewById(R.id.tv_movie_title);
        movieVoteAverage = itemView.findViewById(R.id.tv_vote_average);
        movieCard = itemView.findViewById(R.id.cvMovie);
    }


    public void onBind(MovieResult movie) {

        String url = getMoviePosterUrl(movie);
        if(url!=null) {
            final String finalPosterUrl = Constants.BASE_IMAGE_URL_API + url;
            requestManager.load(finalPosterUrl).into(movieImage);
        }

        movieTitle.setText(movie.getTitle());

        String voteAverage = (movie.getVoteAverage()!=null)? movie.getVoteAverage().toString() : "_._";
        movieVoteAverage.setText(voteAverage);

        movieCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieOnClickListener.onMovieClick(movie.getId());
            }
        });
    }

    private String getMoviePosterUrl(MovieResult movie) {
        String url = null;
        if(movie.getPosterPath()!=null) {
            url = movie.getPosterPath();
        } else if(movie.getBackdropPath()!=null) {
            url = movie.getBackdropPath();
        }

        return url;
    }
}
