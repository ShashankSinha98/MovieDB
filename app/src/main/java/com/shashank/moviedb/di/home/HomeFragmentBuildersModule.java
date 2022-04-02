package com.shashank.moviedb.di.home;

import com.shashank.moviedb.ui.home.HomeFragment;
import com.shashank.moviedb.ui.home.viewholder.MovieViewHolder;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HomeFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract HomeFragment contributeHomeFragment();
}
