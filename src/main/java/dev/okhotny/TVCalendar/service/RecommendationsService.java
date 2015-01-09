package dev.okhotny.TVCalendar.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.graphics.Palette;

import com.squareup.picasso.Picasso;
import com.uwetrottmann.trakt.v2.TraktV2;
import com.uwetrottmann.trakt.v2.entities.Show;
import com.uwetrottmann.trakt.v2.entities.TrendingShow;
import com.uwetrottmann.trakt.v2.enums.Extended;
import com.uwetrottmann.trakt.v2.services.Shows;

import java.io.IOException;
import java.util.List;

import dev.okhotny.TVCalendar.App;
import dev.okhotny.TVCalendar.BuildConfig;
import dev.okhotny.TVCalendar.R;
import dev.okhotny.TVCalendar.ui.ShowDetailsActivity;

public class RecommendationsService extends IntentService {

    private static final int MAX_RECOMMENDATIONS = 5;
    private NotificationManager mNotificationManager;

    public RecommendationsService() {
        super("RecommendationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        TraktV2 trakt = new TraktV2();
        trakt.setApiKey(App.sInstance.getString(R.string.traktv_apikey)).setIsDebug(BuildConfig.DEBUG);
        Shows showService = trakt.shows();
        List<TrendingShow> trending = showService.trending(1, 5, Extended.DEFAULT_MIN);

        int count = 0;

        for (TrendingShow show : trending) {

            try {
                buildRecommendation(getApplicationContext(), show.show);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (++count >= MAX_RECOMMENDATIONS) {
                break;
            }
        }
    }

    public Notification buildRecommendation(Context context, Show movie)
            throws IOException {

        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle extras = new Bundle();


        // build the recommendation as a Notification object
        Bitmap poster = Picasso.with(context).load(movie.images.logo.full).get();

        Palette palette = Palette.generate(poster);

        Notification notification = new NotificationCompat.BigPictureStyle(
                new NotificationCompat.Builder(context)
                        .setContentTitle(movie.title)
                        .setContentText(movie.overview)
                        .setContentInfo(getString(R.string.app_name))
                        .setGroup("TVShows")
                        .setPriority((int) Math.round(movie.rating))
                        .setColor(palette.getDarkVibrantColor(Color.BLACK))
                        .setCategory("recommendation")
                        .setLargeIcon(poster)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentIntent(buildPendingIntent(movie.ids.trakt))
                        .setExtras(extras))
                .build();

        // post the recommendation to the NotificationManager
        mNotificationManager.notify(movie.ids.trakt, notification);
        mNotificationManager = null;
        return notification;
    }

    private PendingIntent buildPendingIntent(long id) {
        Intent detailsIntent = new Intent(this, ShowDetailsActivity.class);
        detailsIntent.putExtra("id", id);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(ShowDetailsActivity.class);
        stackBuilder.addNextIntent(detailsIntent);
        // Ensure each PendingIntent is unique
        detailsIntent.setAction(Long.toString(id));

        PendingIntent intent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT);
        return intent;
    }

}