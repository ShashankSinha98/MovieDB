package com.shashank.moviedb.data.local.typeconverter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shashank.moviedb.model.Credits;
import com.shashank.moviedb.model.Genre;

import java.lang.reflect.Type;
import java.util.List;

public class MovieTypeConverter {

    @TypeConverter
    public static String creditsToString(Credits credits){
        return new Gson().toJson(credits);
    }

    @TypeConverter
    public static String genresToString(List<Genre> genres){
        return new Gson().toJson(genres);
    }

    @TypeConverter
    public static Credits stringToCredits(String creditsJsonString) {
        Type type = new TypeToken<Credits>() {}.getType();
        return new Gson().fromJson(creditsJsonString, type);
    }

    @TypeConverter
    public static List<Genre> stringToGenresList(String genreListJsonString) {
        Type type = new TypeToken<List<Genre>>() {}.getType();
        return new Gson().fromJson(genreListJsonString, type);
    }
}
