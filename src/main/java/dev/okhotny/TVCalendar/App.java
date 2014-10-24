package dev.okhotny.TVCalendar;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class App extends Application {

    public static App sInstance;
    public Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.global_tracker);
    }

}
