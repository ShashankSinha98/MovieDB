package com.shashank.moviedb.di.trending;

import androidx.lifecycle.ViewModel;

import com.shashank.moviedb.di.ViewModelKey;
import com.shashank.moviedb.ui.trending.TrendingViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class HomeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TrendingViewModel.class)
    abstract ViewModel bindHomeViewModel(TrendingViewModel trendingViewModel);
}
