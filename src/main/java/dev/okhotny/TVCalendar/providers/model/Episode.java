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

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author matthew.altman
 */

@DatabaseTable
public class Episode {
    @DatabaseField(index = true, id = true)
    private String id;
    @DatabaseField
    private String episodeName;
    @DatabaseField
    private int episodeNumber;
    @DatabaseField(index = true, dataType = DataType.DATE_LONG)
    private Date firstAired;
    @DatabaseField
    private String imdbId;
    @DatabaseField
    private String overview;
    @DatabaseField
    private float rating;
    @DatabaseField
    private String filename;
    @DatabaseField(index = true)
    private String seriesId;
    @DatabaseField
    private int seasonNumber;
    @DatabaseField
    private boolean isWatched;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(String episodeNumber) {
        try {
            this.episodeNumber = Integer.parseInt(episodeNumber);
        } catch (Exception ignored) {
            this.episodeNumber = 0;
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Date getFirstAired() {
        return firstAired;
    }

    public void setFirstAired(String firstAired) {
        try {
            this.firstAired = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(firstAired);
        } catch (Exception ignored) {
        }
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
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

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(String seasonNumber) {
        try {
            this.seasonNumber = Integer.parseInt(seasonNumber);
        } catch (Exception ignored) {
        }
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }
}
