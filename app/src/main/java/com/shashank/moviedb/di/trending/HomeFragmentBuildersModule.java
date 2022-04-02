package com.shashank.moviedb.di.trending;

import com.shashank.moviedb.ui.trending.TrendingFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HomeFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract TrendingFragment contributeHomeFragment();
}
