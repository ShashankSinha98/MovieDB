package com.shashank.moviedb.di.home;

import androidx.lifecycle.ViewModel;

import com.shashank.moviedb.di.ViewModelKey;
import com.shashank.moviedb.ui.home.HomeViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class HomeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindHomeViewModel(HomeViewModel homeViewModel);
}
