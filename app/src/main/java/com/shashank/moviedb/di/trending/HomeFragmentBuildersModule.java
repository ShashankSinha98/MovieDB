package com.shashank.moviedb.di.trending;

import com.shashank.moviedb.ui.detail.DetailFragment;
import com.shashank.moviedb.ui.favourites.FavouriteFragment;
import com.shashank.moviedb.ui.nowplaying.NowPlayingFragment;
import com.shashank.moviedb.ui.trending.TrendingFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class HomeFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract TrendingFragment contributeHomeFragment();

    @ContributesAndroidInjector
    abstract NowPlayingFragment contributeNowPlayingFragment();

    @ContributesAndroidInjector
    abstract DetailFragment contributeDetailFragment();

    @ContributesAndroidInjector
    abstract FavouriteFragment contributeFavouriteFragment();
}
