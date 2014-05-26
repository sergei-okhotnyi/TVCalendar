/*
 *      Copyright (c) 2004-2013 Matthew Altman & Stuart Boston
 *
 *      This file is part of TheTVDB API.
 *
 *      TheTVDB API is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      TheTVDB API is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with TheTVDB API.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package dev.okhotny.TVCalendar.providers.model;

import android.os.AsyncTask;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dev.okhotny.TVCalendar.App;
import dev.okhotny.TVCalendar.database.DatabaseHelper;

/**
 * @author altman.matthew
 */

@DatabaseTable
public class Series {

    @DatabaseField(id = true)
    private String id;
    @DatabaseField
    private String seriesName;
    @DatabaseField
    private String banner;
    @DatabaseField
    private String overview;
    @DatabaseField
    private String firstAired;
    @DatabaseField
    private String imdbId;
    @DatabaseField
    private String zap2ItId;
    @DatabaseField
    private String airsDayOfWeek;
    @DatabaseField
    private String airsTime;
    @DatabaseField
    private String contentRating;
    @DatabaseField
    private String genres;
    @DatabaseField
    private String network;
    @DatabaseField
    private float rating;
    @DatabaseField
    private String runtime;
    @DatabaseField
    private String status;
    @DatabaseField
    private String fanart;
    @DatabaseField
    private String poster;
    @DatabaseField
    private String actors;
    private List<Episode> mEpisodes = new ArrayList<Episode>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAirsDayOfWeek() {
        return airsDayOfWeek;
    }

    public void setAirsDayOfWeek(String airsDayOfWeek) {
        this.airsDayOfWeek = airsDayOfWeek;
    }

    public String getAirsTime() {
        return airsTime;
    }

    public void setAirsTime(String airsTime) {
        this.airsTime = airsTime;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public String getFanart() {
        return fanart;
    }

    public void setFanart(String fanart) {
        this.fanart = fanart;
    }

    public String getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(String firstAired) {
        this.firstAired = firstAired;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(String rating) {
        try {
            this.rating = Float.parseFloat(rating);
        } catch (Exception ignore) {
            this.rating = 0;
        }
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getZap2ItId() {
        return zap2ItId;
    }

    public void setZap2ItId(String zap2ItId) {
        this.zap2ItId = zap2ItId;
    }

    public void addAsFavorite(final boolean mIsItemFavored) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (mIsItemFavored) {
                    DatabaseHelper databaseHelper = OpenHelperManager.getHelper(App.sInstance, DatabaseHelper.class);
                    try {
                        databaseHelper.getSeriesDao().createIfNotExists(Series.this);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        OpenHelperManager.releaseHelper();
                    }
                } else {
                    DatabaseHelper databaseHelper = OpenHelperManager.getHelper(App.sInstance, DatabaseHelper.class);
                    try {
                        databaseHelper.getSeriesDao().deleteById(getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        OpenHelperManager.releaseHelper();
                    }
                }
                return null;
            }
        }.execute();
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public List<Episode> getEpisodes() {
        return mEpisodes;
    }

}
