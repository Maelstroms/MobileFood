package edu.neu.madcourse.dhvanisheth.twoplayerwordgame;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Dhvani on 3/22/2016.
 */
public class CheckInternetConnectivity {

    public boolean CheckConnectivity(final Context c) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager.getActiveNetworkInfo() != null
                && mConnectivityManager.getActiveNetworkInfo().isAvailable()
                && mConnectivityManager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
