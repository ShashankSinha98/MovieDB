package com.shashank.moviedb.data;

public interface ResourceCallback<T> {
    void onResponse(Resource<T> resource);
}
