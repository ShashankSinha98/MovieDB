package com.shashank.moviedb.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Resource<T> {


    @NonNull private Status status;
    @Nullable private T data;
    @Nullable private String message;

    public Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }


    public static <T> Resource success(@NonNull T data) {
        return new Resource(Status.SUCCESS, data, null);
    }

    public static <T> Resource error(@NonNull String message, @Nullable T data) {
        return new Resource(Status.ERROR, data, message);
    }

    public static <T> Resource loading(@Nullable T data) {
        return new Resource(Status.LOADING, data, null);
    }


    @NonNull
    public Status getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    enum Status {
        SUCCESS,ERROR, LOADING
    }

}
