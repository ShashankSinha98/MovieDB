package com.shashank.moviedb.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ShareCompat;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.google.android.material.snackbar.Snackbar;
import com.shashank.moviedb.R;
import com.shashank.moviedb.common.ViewModelProviderFactory;
import com.shashank.moviedb.data.Resource;
import com.shashank.moviedb.model.Genre;
import com.shashank.moviedb.model.MovieDetail;
import com.shashank.moviedb.model.MovieResult;
import com.shashank.moviedb.ui.detail.adapter.CastRecyclerAdapter;
import com.shashank.moviedb.util.Constants;

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class DetailFragment extends DaggerFragment implements View.OnClickListener {

    private static final String TAG = "DetailFragment";
    private AppCompatImageView ivBackdrop, ivPoster, ivFavorite, noInternetIcon, shareIcon, ivTrailer;
    private ProgressBar progressBar;
    private TextView tvMovieTitle, tvVoteAverage, tvDuration, tvGenre, tvDescription, tvQuote, tvDescriptionTitle, tvCastTitle, tvTaglineTitle;
    private RecyclerView castRecyclerView;
    private ConstraintLayout detailLayout;
    private NestedScrollView nestedScrollView;
    private Snackbar snackbar;

    @Inject public ViewModelProviderFactory providerFactory;
    @Inject public RequestManager requestManager;
    @Inject public CastRecyclerAdapter castRecyclerAdapter;
    private DetailViewModel detailViewModel;
    private Long movieId;

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

        movieId = DetailFragmentArgs.fromBundle(getArguments()).getMovieId();
        detailViewModel.getMovieDetail(movieId);
    }

    private void initObservers() {
        detailViewModel.getMovieDetailLiveData().observe(getViewLifecycleOwner(), new Observer<Resource<MovieDetail>>() {
            @Override
            public void onChanged(Resource<MovieDetail> movieDetailResource) {
                switch (movieDetailResource.getStatus()) {
                    case SUCCESS:
                        showProgressBar(false);
                        MovieDetail movieDetail = ((MovieDetail)movieDetailResource.getData());
                        updateLayoutForMovieResult(View.VISIBLE);
                        updateLayoutWithMovieDetailData(movieDetail);
                        break;

                    case LOADING:
                        showProgressBar(true);
                        break;
                } 
            }
        });

        detailViewModel.getFavouriteIconStatus().observe(getViewLifecycleOwner(), new Observer<Resource<Boolean>>() {
            @Override
            public void onChanged(Resource<Boolean> booleanResource) {
                switch (booleanResource.getStatus()) {
                    case SUCCESS:
                        boolean isFavorite = booleanResource.getData();
                        ivFavorite.setImageResource((isFavorite)? R.drawable.ic_favorite_black_24dp : R.drawable.ic_favorite_border_black_24dp);
                        break;
                }
            }
        });

        detailViewModel.getMovieResultLiveData().observe(getViewLifecycleOwner(), new Observer<Resource<List<MovieResult>>>() {
            @Override
            public void onChanged(Resource<List<MovieResult>> listResource) {
                switch (listResource.getStatus()) {
                    case SUCCESS:
                        Log.d(TAG, "xlr8: getMovieResultLiveData SUCCESS");
                        showProgressBar(false);
                        MovieResult movieResult = listResource.getData().get(0);
                        updateLayoutForMovieResult(View.INVISIBLE);
                        updateLayoutWithMovieResultData(movieResult);
                        showSnackBar();
                        break;

                    case ERROR:
                        Log.d(TAG, "xlr8: getMovieResultLiveData ERROR: e: "+listResource.getMessage());
                        break;
                }
            }
        });

    }

    private void showSnackBar() {
         snackbar =Snackbar.make(nestedScrollView, "No Connection", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        detailViewModel.getMovieDetail(movieId);
                    }
                }, 250);
            }
        });

        snackbar.show();
    }

    private void updateLayoutWithMovieResultData(MovieResult movieResult) {
        Log.d(TAG,"xlr8: updateLayoutWithMovieResultData movieResult: "+movieResult);
        detailLayout.setVisibility(View.VISIBLE);
        tvMovieTitle.setText(movieResult.getTitle());
        tvMovieTitle.setSelected(true);
        tvVoteAverage.setText(movieResult.getVoteAverage().toString());

        requestManager.load(Constants.BASE_IMAGE_URL_API+movieResult.getPosterPath())
                .into(ivPoster);

        requestManager.load(Constants.BASE_IMAGE_URL_w500_API+movieResult.getBackdropPath())
                .into(ivBackdrop);

    }



    private void updateLayoutWithMovieDetailData(MovieDetail movieDetail) {
        Log.d(TAG,"xlr8: updateLayoutWithMovieDetailData movieDetail: "+movieDetail);
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
        tvDescriptionTitle = view.findViewById(R.id.tvDescriptionTitle);
        ivFavorite = view.findViewById(R.id.ivFavorite);
        tvCastTitle = view.findViewById(R.id.tvCastTitle);
        tvTaglineTitle = view.findViewById(R.id.tvTaglineTitle);
        progressBar = view.findViewById(R.id.progress_bar);
        noInternetIcon = view.findViewById(R.id.no_internet_cloud_icon);
        nestedScrollView = view.findViewById(R.id.nestedDetail);
        shareIcon = view.findViewById(R.id.ivShareIcon);
        ivTrailer = view.findViewById(R.id.ivTrailerIcon);

        shareIcon.setOnClickListener(this);
        ivFavorite.setOnClickListener(this);
        ivTrailer.setOnClickListener(this);
    }

    private void initUI() {
        castRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        castRecyclerView.setAdapter(castRecyclerAdapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ivFavorite:
                detailViewModel.updateFavourite(movieId);
                break;

            case R.id.ivShareIcon:
                shareMovieDeepLink(movieId);
                break;

            case R.id.ivTrailerIcon:
                searchMovieTrailer(tvMovieTitle);
                break;
        }
    }

    private void searchMovieTrailer(TextView tvMovieTitle) {
        String movieTitle = null;
        if(tvMovieTitle!=null) {
            movieTitle = tvMovieTitle.getText().toString();
        }

        if(movieTitle==null || TextUtils.isEmpty(movieTitle)) return;

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_MOVIE_TRAILER_SEARCH+movieTitle+"+trailer")));
    }

    private void shareMovieDeepLink(Long movieId) {
        if(movieId==null) return;

        String link = Constants.DEEP_LINK_TEMPLATE + movieId;
        String msg = Constants.DEEP_LINK_MSG + link;
        Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText(msg)
                .getIntent();

        if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(shareIntent);
        }
    }

    private void updateLayoutForMovieResult(int visible) {
        Log.d(TAG ,"xlr8: updateLayoutForMovieResult called, visible: "+(visible==View.VISIBLE));
        tvGenre.setVisibility(visible);
        tvDuration.setVisibility(visible);
        tvDescriptionTitle.setVisibility(visible);
        tvDescription.setVisibility(visible);
        tvCastTitle.setVisibility(visible);
        castRecyclerView.setVisibility(visible);
        tvTaglineTitle.setVisibility(visible);
        tvQuote.setVisibility(visible);
        
        if(visible==View.INVISIBLE) {
            noInternetIcon.setVisibility(View.VISIBLE);
        } else {
            noInternetIcon.setVisibility(View.INVISIBLE);
        }
    }
    
    private void showProgressBar(boolean show) {
        if(show)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(snackbar!=null) {
            snackbar.dismiss();
        }
    }
}
