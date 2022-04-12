package com.shashank.moviedb.util;

import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.os.Handler;

// Provide threads for Network/DB operations
public class AppExecutors {

    private AppExecutors appExecutors = null;

    // for db operations
    private Executor mDiskIO;

    // For returning data from background thread to main thread
    private Executor mMainThreadExecutor;


    public AppExecutors getInstance() {
        if(appExecutors==null) {
            appExecutors = new AppExecutors();
        }
        return appExecutors;
    }

    private AppExecutors() {
        mDiskIO = Executors.newSingleThreadExecutor();
        mMainThreadExecutor = new MainThreadExecutor();
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor mainThread() {
        return mMainThreadExecutor;
    }

    private class MainThreadExecutor implements Executor {

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable runnable) {
            if(runnable!=null) {
                mainThreadHandler.post(runnable);
            }
        }
    }
}
