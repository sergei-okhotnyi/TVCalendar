package dev.okhotny.TVCalendar;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.uservoice.uservoicesdk.Config;
import com.uservoice.uservoicesdk.UserVoice;

public class App extends Application {

    public static App sInstance;
    public Tracker tracker;

    public RequestQueue requestQueue;
    public ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        tracker = GoogleAnalytics.getInstance(this).newTracker(R.xml.global_tracker);
        requestQueue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(requestQueue, new DiskBitmapCache(this));

        Config config = new Config("tv-calendar.uservoice.com");
        config.setForumId(253265);
        UserVoice.init(config, this);
    }

}
