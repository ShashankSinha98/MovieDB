package com.shashank.moviedb.di.trending;

import androidx.lifecycle.ViewModel;

import com.shashank.moviedb.di.ViewModelKey;
import com.shashank.moviedb.ui.detail.DetailViewModel;
import com.shashank.moviedb.ui.nowplaying.NowPlayingViewModel;
import com.shashank.moviedb.ui.trending.TrendingViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class HomeViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TrendingViewModel.class)
    abstract ViewModel bindTrendingViewModel(TrendingViewModel trendingViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(NowPlayingViewModel.class)
    abstract ViewModel bindNowPlayingViewModel(NowPlayingViewModel nowPlayingViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel.class)
    abstract ViewModel bindDetailViewModel(DetailViewModel detailViewModel);
}
