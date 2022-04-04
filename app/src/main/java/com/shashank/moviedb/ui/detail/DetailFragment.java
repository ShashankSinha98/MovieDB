package com.shashank.moviedb.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.shashank.moviedb.R;
import com.shashank.moviedb.common.ViewModelProviderFactory;
import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.model.Genre;
import com.shashank.moviedb.model.MovieDetail;
import com.shashank.moviedb.ui.detail.adapter.CastRecyclerAdapter;
import com.shashank.moviedb.ui.trending.TrendingViewModel;
import com.shashank.moviedb.util.Constants;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class DetailFragment extends DaggerFragment {

    private static final String TAG = "DetailFragment";
    private AppCompatImageView ivBackdrop, ivPoster;
    private TextView tvMovieTitle, tvVoteAverage, tvDuration, tvGenre, tvDescription, tvQuote;
    private RecyclerView castRecyclerView;
    private ConstraintLayout detailLayout;

    @Inject public ViewModelProviderFactory providerFactory;
    @Inject public RequestManager requestManager;
    @Inject public CastRecyclerAdapter castRecyclerAdapter;
    private DetailViewModel detailViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_detail, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailViewModel = new ViewModelProvider(this, providerFactory).get(DetailViewModel.class);
        initViews(view);
        initUI();
        initObservers();

        Long movieId = DetailFragmentArgs.fromBundle(getArguments()).getMovieId();
        detailViewModel.getMovieDetail(movieId);
    }

    private void initObservers() {
        detailViewModel.getMovieDetailLiveData().observe(getViewLifecycleOwner(), new Observer<Resource<MovieDetail>>() {
            @Override
            public void onChanged(Resource<MovieDetail> movieDetailResource) {
                switch (movieDetailResource.getStatus()) {
                    case SUCCESS:
                        MovieDetail movieDetail = ((MovieDetail)movieDetailResource.getData());
                        updateLayoutWithMovieData(movieDetail);
                        break;
                } 
            }
        });
    }

    private void updateLayoutWithMovieData(MovieDetail movieDetail) {
        Log.d(TAG,"xlr8: movieDetail: "+movieDetail);
        detailLayout.setVisibility(View.VISIBLE);
        tvMovieTitle.setText(movieDetail.getTitle());
        tvMovieTitle.setSelected(true);
        tvVoteAverage.setText(movieDetail.getVoteAverage().toString());
        tvDuration.setText(getDurationString(movieDetail.getRuntime()));
        tvGenre.setText(getGenreString(movieDetail.getGenres()));
        tvDescription.setText(movieDetail.getOverview());
        tvQuote.setText(movieDetail.getTagline());

        requestManager.load(Constants.BASE_IMAGE_URL_API+movieDetail.getPosterPath())
                .into(ivPoster);

        requestManager.load(Constants.BASE_IMAGE_URL_w500_API+movieDetail.getBackdropPath())
                .into(ivBackdrop);
        if(movieDetail.getCredits()!=null && movieDetail.getCredits().getCast()!=null)
            castRecyclerAdapter.setCast(movieDetail.getCredits().getCast());
    }

    private String getDurationString(Integer duration) {

        if(duration==null) return Constants.CONST_DATA_NA;

        int hours = duration/60;
        int minutes = duration%60;

        return String.format(" %sh %smin", hours, minutes);
    }

    private String getGenreString(List<Genre> genres) {
        if(genres==null || genres.isEmpty()) return Constants.CONST_DATA_NA;

        String genreStr = "";
        for(Genre genre: genres) {
            genreStr += genre.getName() +", ";
        }

        return genreStr.substring(0,genreStr.length()-1);
    }

    private void initViews(View view) {
        ivBackdrop = view.findViewById(R.id.ivBackdrop);
        ivPoster = view.findViewById(R.id.ivPoster);
        tvMovieTitle = view.findViewById(R.id.tvMovieTitleValue);
        tvVoteAverage = view.findViewById(R.id.tvVoteAverage);
        tvDuration = view.findViewById(R.id.tvDuration);
        tvGenre = view.findViewById(R.id.tvGenreValue);
        tvDescription = view.findViewById(R.id.tvDescriptionValue);
        tvQuote = view.findViewById(R.id.tvQuoteValue);
        castRecyclerView = view.findViewById(R.id.rvCast);
        detailLayout = view.findViewById(R.id.clDetail);
    }

    private void initUI() {
        castRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        castRecyclerView.setAdapter(castRecyclerAdapter);
    }
}
