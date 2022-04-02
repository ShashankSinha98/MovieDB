package com.shashank.moviedb.util;

import com.shashank.moviedb.BuildConfig;
import com.shashank.moviedb.model.MovieResponse;

import java.util.HashMap;
import java.util.Map;

public abstract class Constants {

    public static final String BASE_URL = "https://api.themoviedb.org";
    private static final String API_KEY = BuildConfig.API_KEY;
    public static final String LANGUAGE = "en-US";
    public static final String REGION = "IN";


    public static class QueryString {
        public static final String API_KEY = "api_key";
        public static final String LANGUAGE = "language";
        public static final String REGION = "region";
    }

    public static class QueryParams {
        public static final Map<String, String> PARAMS_NOW_PLAYING_MOVIE_API = new HashMap() {
            { put(QueryString.API_KEY, API_KEY);
              put(QueryString.LANGUAGE, LANGUAGE);
              put(QueryString.REGION, REGION);}
        };
    }

}
