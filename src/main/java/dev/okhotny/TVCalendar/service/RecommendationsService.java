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

import com.jakewharton.trakt.Trakt;
import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.services.ShowService;
import com.squareup.picasso.Picasso;

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
        Trakt trakt = new Trakt();
        trakt.setApiKey(App.sInstance.getString(R.string.traktv_apikey)).setIsDebug(BuildConfig.DEBUG);
        ShowService showService = trakt.showService();
        List<TvShow> trending = showService.trending();

        int count = 0;

        for (TvShow show : trending) {

            try {
                buildRecommendation(getApplicationContext(), show);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (++count >= MAX_RECOMMENDATIONS) {
                break;
            }
        }
    }

    public Notification buildRecommendation(Context context, TvShow movie)
            throws IOException {

        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle extras = new Bundle();


        // build the recommendation as a Notification object
        Bitmap poster = Picasso.with(context).load(movie.images.poster).get();

        Palette palette = Palette.generate(poster);

        Notification notification = new NotificationCompat.BigPictureStyle(
                new NotificationCompat.Builder(context)
                        .setContentTitle(movie.title)
                        .setContentText(movie.overview)
                        .setContentInfo(getString(R.string.app_name))
                        .setGroup("TVShows")
                        .setPriority(movie.ratings.percentage)
                        .setColor(palette.getDarkVibrantColor(Color.BLACK))
                        .setCategory("recommendation")
                        .setLargeIcon(poster)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentIntent(buildPendingIntent(movie.tvdb_id))
                        .setExtras(extras))
                .build();

        // post the recommendation to the NotificationManager
        mNotificationManager.notify(movie.tvdb_id, notification);
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