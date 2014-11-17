package dev.okhotny.TVCalendar.model;

import com.omertron.thetvdbapi.model.Episode;
import com.omertron.thetvdbapi.model.ResponseData;
import com.omertron.thetvdbapi.model.Series;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

public class TvDbSeries extends Series implements Serializable {
    private static final long serialVersionUID = 1L;

    public HashSet<Integer> seasons = new HashSet<Integer>();
    public List<Episode> episodes;

    public TvDbSeries() {
    }

    public TvDbSeries(ResponseData data) {
        Series series = data.serieses.get(0);
        this.id = series.id;
        this.seriesID = series.seriesID;
        this.language = series.language;
        this.seriesName = series.seriesName;
        this.overview = series.overview;
        this.firstAired = series.firstAired;
        this.imdbId = series.imdbId;
        this.zap2ItId = series.zap2ItId;
        this.actors = series.actors;
        this.airsDayOfWeek = series.airsDayOfWeek;
        this.airsTime = series.airsTime;
        this.contentRating = series.contentRating;
        this.genres = series.genres;
        this.network = series.network;
        this.rating = series.rating;
        this.runtime = series.runtime;
        this.status = series.status;
        this.banner = series.banner;
        this.fanart = series.fanart;
        this.poster = series.poster;
        this.lastUpdated = series.lastUpdated;

        if (data.episodes != null) {
            episodes = data.episodes;
            for (Episode e : data.episodes) {
                seasons.add(e.seasonNumber);
            }
        }
    }
}
