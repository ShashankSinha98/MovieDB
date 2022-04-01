package com.shashank.moviedb.di;

import com.shashank.moviedb.ui.HomeActivity;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract HomeActivity contributeHomeActivity();
}
