package com.shashank.moviedb.util;

import com.shashank.moviedb.BuildConfig;
import com.shashank.moviedb.model.MovieResponse;

import java.util.HashMap;
import java.util.Map;

public abstract class Constants {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = BuildConfig.API_KEY;
    public static final String LANGUAGE = "en-US";
    public static final String REGION = "IN";
    public static final Long READ_TIMEOUT = 2000L;

    public static final String INVALID_MOVIE_ID_ERROR_MSG = "Invalid Movie ID";



    public static class QueryString {
        public static final String API_KEY = "api_key";
        public static final String LANGUAGE = "language";
        public static final String REGION = "region";
        public static final String APPEND_TO_RESPONSE = "append_to_response";
    }

    public static class QueryParams {
        public static final Map<String, String> PARAMS_NOW_PLAYING_MOVIE_API = new HashMap() {
            { put(QueryString.API_KEY, API_KEY);
              put(QueryString.LANGUAGE, LANGUAGE);
              put(QueryString.REGION, REGION);}
        };

        public static final Map<String, String> PARAMS_TRENDING_MOVIE_API =  new HashMap() {
            { put(QueryString.API_KEY, API_KEY); }
        };

        public static final Map<String, String> PARAMS_MOVIE_DETAIL_API = new HashMap() {
            { put(QueryString.API_KEY, API_KEY);
                put(QueryString.LANGUAGE, LANGUAGE);
                put(QueryString.APPEND_TO_RESPONSE, "credits");}
        };
    }

    public static final String TRENDING_TIME_WINDOW = "week"; // week or day

}
