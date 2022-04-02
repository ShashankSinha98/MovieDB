package com.shashank.moviedb.di.home;

import android.app.Application;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.shashank.moviedb.ui.home.adapter.MovieRecyclerAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {

    @HomeScope
    @Provides
    public static MovieRecyclerAdapter provideMovieRecyclerAdapter(RequestManager requestManager) {
        return new MovieRecyclerAdapter(requestManager);
    }

    @HomeScope
    @Provides
    public static RecyclerView.LayoutManager provideGridLayoutManager(Application application) {
        return new GridLayoutManager(application, 2);
    }
}
