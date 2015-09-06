package com.moyheen.popularmovies1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by moyheen on 9/5/2015.
 * This class determines whether or not the device has a working internet connection
 */
public class NetworkConnection {
    private Context mContext;

    public NetworkConnection(Context context) {
        this.mContext = context;
    }

    /**
     * Determines the connectivity status of the device
     */
    public final boolean isInternetOn() {
        boolean hasConnectedWifi = false;
        boolean hasConnectedMobile = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)
                mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo netInfo : networkInfo) {
            if (netInfo.getTypeName().equalsIgnoreCase("WIFI")) {
                if (netInfo.isConnected()) {
                    hasConnectedWifi = true;
                }
            }
            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (netInfo.isConnected()) {
                    hasConnectedMobile = true;
                }
            }
        }
        return hasConnectedMobile || hasConnectedWifi;
    }

}
