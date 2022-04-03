package com.shashank.moviedb.di.trending;

import android.app.Application;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.shashank.moviedb.ui.detail.adapter.CastRecyclerAdapter;
import com.shashank.moviedb.ui.trending.adapter.MovieRecyclerAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {

    @HomeScope
    @Provides
    public static CastRecyclerAdapter provideCastRecyclerAdapter(RequestManager requestManager) {
        return new CastRecyclerAdapter(requestManager);
    }

}
