package com.shashank.moviedb.common;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class NetworkHelper {

    private Application application;

    public NetworkHelper(Application application) {
        this.application = application;
    }

    public Boolean isNetworkConnected() {
        boolean isNetworkConnected = false;
        ConnectivityManager connectivityManager =
                ((ConnectivityManager)application.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = connectivityManager.getActiveNetwork();
            if(network==null) return false;
            NetworkCapabilities networkCapabilities =
                    connectivityManager.getNetworkCapabilities(network);
            if(networkCapabilities==null) return false;

            if(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                isNetworkConnected =  true;
            } else isNetworkConnected = false;

        } else {

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null) {
                isNetworkConnected = networkInfo.isConnected();
            }
        }
        return isNetworkConnected;
    }
}
