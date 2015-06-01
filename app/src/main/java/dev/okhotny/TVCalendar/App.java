package dev.okhotny.TVCalendar;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import dev.okhotny.TVCalendar.service.AnalyticsTrackers;
import io.fabric.sdk.android.Fabric;

public class App extends Application {

    public static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sInstance = this;
        AnalyticsTrackers.initialize(this);
        if (BuildConfig.DEBUG) {
            Picasso.with(this).setLoggingEnabled(true);
            Picasso.with(this).setIndicatorsEnabled(true);
        }
    }

}
