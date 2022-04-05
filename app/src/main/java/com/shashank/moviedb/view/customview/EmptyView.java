package com.shashank.moviedb.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.shashank.moviedb.BaseApplication;
import com.shashank.moviedb.R;
import com.shashank.moviedb.data.Status;

public class EmptyView extends ConstraintLayout {

    private static final String TAG = "EmptyView";

    public static final int LOTTIE_ANIM_NO_INTERNET = 0;
    public static final int LOTTIE_ANIM_ERROR = 1;
    public static final int LOTTIE_ANIM_LOADING = 2;
    public static final int LOTTIE_ANIM_NO_FAVOURITE = 3;

    private ConstraintLayout rootView;
    private AppCompatButton btnretry;
    private LottieAnimationView animationView;
    private TextView tvTitle;
    private TextView tvDesc;


    public EmptyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.layout_empty_view, this);

        rootView = findViewById(R.id.cl_root);
        btnretry = findViewById(R.id.btn_retry);
        animationView = findViewById(R.id.lottie_anim);
        tvTitle = findViewById(R.id.tv_title);
        tvDesc = findViewById(R.id.tv_desc);
    }

    public void update(Status status, boolean isNetworkAvail, OnClickListener listener) {
        Log.d(TAG, "xlr8: Status: "+status);

        switch (status) {

            case LOADING:
                setUpLottieAnimFile(LOTTIE_ANIM_LOADING);
                showAnimationOnly(true);
                showRootView(true);
                break;


            case ERROR:
                if(isNetworkAvail)
                    setUpLottieAnimFile(LOTTIE_ANIM_ERROR);
                else
                    setUpLottieAnimFile(LOTTIE_ANIM_NO_INTERNET);
                showRootView(true);
                showAnimationOnly(false);
                btnretry.setOnClickListener(listener);
                break;

            case SUCCESS:
                showRootView(false);
                break;
        }
    }

    public void updateFavourite(Status status, Integer favouriteCount, OnClickListener listener) {
        Log.d(TAG, "xlr8: Status: "+status+", favouriteCount: "+favouriteCount);

        switch (status) {

            case LOADING:
                setUpLottieAnimFile(LOTTIE_ANIM_LOADING);
                showAnimationOnly(true);
                showRootView(true);
                break;


            case ERROR:
                setUpLottieAnimFile(LOTTIE_ANIM_ERROR);
                showRootView(true);
                showAnimationOnly(false);
                btnretry.setOnClickListener(listener);
                break;

            case SUCCESS:
                if(favouriteCount==0) {
                    showRootView(true);
                    setUpLottieAnimFile(LOTTIE_ANIM_NO_FAVOURITE);
                    showAnimationOnly(false);
                    showRetryButton(false);
                } else {
                    showRootView(false);
                }
                break;
        }
    }


    private void setUpLottieAnimFile(int animType) {
        try {
            switch (animType) {
                case LOTTIE_ANIM_NO_INTERNET:
                    animationView.setAnimation("no_internet_1.json");
                    break;

                case LOTTIE_ANIM_LOADING:
                    animationView.setAnimation("cinema_animation.json");
                    break;

                case LOTTIE_ANIM_ERROR:
                    animationView.setAnimation("error_anim_1.json");
                    break;

                case LOTTIE_ANIM_NO_FAVOURITE:
                    animationView.setAnimation("watch_a_movie_with_popcorn.json");
                    break;

            }
            animationView.loop(true);
            animationView.playAnimation();
        } catch (Exception e) {
            Log.d(TAG, "xlr8: EXCEPTION: setUpLoadingLottieAnimFile");
        }
    }

    private void showRootView(boolean visible) {
        if(visible)
            rootView.setVisibility(VISIBLE);
        else
            rootView.setVisibility(GONE);
    }

    private void showAnimationOnly(boolean onlyAnimation) {
        if(onlyAnimation) {
            tvTitle.setVisibility(GONE);
            tvDesc.setVisibility(GONE);
            btnretry.setVisibility(GONE);
        } else {
            tvTitle.setVisibility(VISIBLE);
            tvDesc.setVisibility(VISIBLE);
            btnretry.setVisibility(VISIBLE);
        }
    }

    private void showRetryButton(boolean showBtn) {
        if(showBtn)
            btnretry.setVisibility(VISIBLE);
        else
            btnretry.setVisibility(GONE);
    }
}
