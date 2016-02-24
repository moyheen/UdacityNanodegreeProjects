package com.moyinoluwa.gdgngevents;

import android.app.Application;

import com.batch.android.Batch;
import com.batch.android.Config;
import com.firebase.client.Firebase;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by moyinoluwa on 2/14/16.
 */
public class GDGNGEventsApplication extends Application {
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

        // Enables disk persistence for Firebase
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

        Batch.Push.setGCMSenderId(getResources().getString(R.string.gcm_sender_id));
        Batch.setConfig(new Config(getResources().getString(R.string.batch_api_key)));
        // Set the color of the notification circle
        Batch.Push.setNotificationsColor(R.color.colorPrimary);
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
