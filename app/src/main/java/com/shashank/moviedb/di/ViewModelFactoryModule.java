package com.shashank.moviedb.di;

import androidx.lifecycle.ViewModelProvider;

import com.shashank.moviedb.common.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory factory);
}
