package com.shashank.moviedb.util;

import android.util.Log;

public class ELog {

    private static final String TAG = "ELog";
    private static final String UNIQUE_TAG = "xlr8";
    private static final String TARGET_CLASS = null;

    public static void d(String TAG, Object msg) {
        try {
            if(TARGET_CLASS==null || TAG.equals(TARGET_CLASS)) {
                Log.d(TAG, UNIQUE_TAG+": "+msg);
            }
        } catch (Exception e) {
            Log.d(TAG, UNIQUE_TAG+" EXCEPTION, e: "+e.getMessage());
            e.printStackTrace();
        }
    }

}
