package dev.okhotny.TVCalendar;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

public class App extends Application {

    public static App sInstance;
    public Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.global_tracker);
        if (BuildConfig.DEBUG) {
            Picasso.with(this).setLoggingEnabled(true);
            Picasso.with(this).setIndicatorsEnabled(true);
        }
    }

}
