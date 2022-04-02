package com.shashank.moviedb.di;

import com.shashank.moviedb.data.remote.MovieApi;
import com.shashank.moviedb.data.remote.MovieRepository;
import com.shashank.moviedb.data.remote.MovieRepositoryImpl;
import com.shashank.moviedb.util.Constants;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Singleton
    @Provides
    public static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient().newBuilder()
                .readTimeout(Constants.READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    @Singleton
    @Provides
    public static Retrofit provideRetrofitInstance(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }


    @Singleton
    @Provides
    public static MovieApi provideMovieApi(Retrofit retrofit) {
        return retrofit.create(MovieApi.class);
    }

    @Singleton
    @Provides
    public static MovieRepository provideMovieRepository(MovieApi movieApi) {
        return new MovieRepositoryImpl(movieApi);
    }
}
