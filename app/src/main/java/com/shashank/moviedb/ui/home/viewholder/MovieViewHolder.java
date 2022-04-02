package com.shashank.moviedb.ui.home.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.shashank.moviedb.R;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.util.Constants;

import javax.inject.Inject;

public class MovieViewHolder extends RecyclerView.ViewHolder {


    private RequestManager requestManager;

    private AppCompatImageView movieImage;
    private TextView movieTitle;
    private TextView movieVoteAverage;

    public MovieViewHolder(@NonNull View itemView, RequestManager requestManager) {
        super(itemView);
        this.requestManager = requestManager;
        movieImage = itemView.findViewById(R.id.iv_movie);
        movieTitle = itemView.findViewById(R.id.tv_movie_title);
        movieVoteAverage = itemView.findViewById(R.id.tv_vote_average);
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
    }

    private String getMoviePosterUrl(MovieResult movie) {
        String url = null;
        if(movie.getBackdropPath()!=null) {
            url = movie.getBackdropPath();
        } else if(movie.getPosterPath()!=null) {
            url = movie.getPosterPath();
        }

        return url;
    }
}
