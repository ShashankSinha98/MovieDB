package com.shashank.moviedb.di;

import com.shashank.moviedb.di.home.HomeFragmentBuildersModule;
import com.shashank.moviedb.di.home.HomeModule;
import com.shashank.moviedb.di.home.HomeScope;
import com.shashank.moviedb.di.home.HomeViewModelModule;
import com.shashank.moviedb.ui.HomeActivity;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @HomeScope
    @ContributesAndroidInjector(
            modules = {HomeModule.class, HomeFragmentBuildersModule.class, HomeViewModelModule.class}
    )
    abstract HomeActivity contributeHomeActivity();
}
