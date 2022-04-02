package com.shashank.moviedb.di;

import com.shashank.moviedb.di.trending.HomeFragmentBuildersModule;
import com.shashank.moviedb.di.trending.HomeModule;
import com.shashank.moviedb.di.trending.HomeScope;
import com.shashank.moviedb.di.trending.HomeViewModelModule;
import com.shashank.moviedb.ui.HomeActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @HomeScope
    @ContributesAndroidInjector(
            modules = {HomeModule.class, HomeFragmentBuildersModule.class, HomeViewModelModule.class}
    )
    abstract HomeActivity contributeHomeActivity();
}
