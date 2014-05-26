package dev.okhotny.TVCalendar.providers.thetvdb;

import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import dev.okhotny.TVCalendar.App;
import dev.okhotny.TVCalendar.database.DatabaseHelper;
import dev.okhotny.TVCalendar.providers.SimpleCallback;
import dev.okhotny.TVCalendar.providers.model.Episode;
import dev.okhotny.TVCalendar.providers.model.Series;

public class Synchronizer {

    private boolean mIsRunning;

    public static void sync(boolean force) {
        final Calendar today = Calendar.getInstance();

        if (!force && (today.getTimeInMillis() - PreferenceManager.getDefaultSharedPreferences(App.sInstance).getLong("last_sync", 0)) < DateUtils.WEEK_IN_MILLIS) {
            return;
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                PreferenceManager.getDefaultSharedPreferences(App.sInstance).edit().putLong("last_sync", today.getTimeInMillis());
                DatabaseHelper helper = OpenHelperManager.getHelper(App.sInstance, DatabaseHelper.class);
                try {
                    final Dao<Series, String> seriesDao = helper.getSeriesDao();
                    final Dao<Episode, String> episodeDao = helper.getEpisodeDao();
                    for (Series series : seriesDao.queryForAll()) {
                        TheTVDBApi.getSeries(series.getId(), true, new SimpleCallback<Series>() {
                            @Override
                            public void done(Series result) {
                                if (result != null) {
                                    try {
                                        seriesDao.update(result);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        TheTVDBApi.getAllEpisodes(series.getId(), true, new SimpleCallback<List<Episode>>() {
                            @Override
                            public void done(List<Episode> result) {
                                for (Episode episode : result) {
                                    try {
                                        Episode old = episodeDao.queryForId(episode.getId());
                                        if (old != null) {
                                            episode.setWatched(old.isWatched());
                                        }
                                        episodeDao.createOrUpdate(episode);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    public interface Callback {
        void onProgress(int progress);

        void onDone();
    }
}
