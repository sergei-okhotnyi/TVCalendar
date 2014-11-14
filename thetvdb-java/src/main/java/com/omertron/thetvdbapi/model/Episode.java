package com.omertron.thetvdbapi.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(strict = false)
public class Episode implements Serializable {

    // Default serial UID
    private static final long serialVersionUID = 1L;
    @Element(name = "id", required = false)
    public String id;
    @Element(name = "Combined_episodenumber", required = false)
    public String combinedEpisodeNumber;
    @Element(name = "Combined_season", required = false)
    public String combinedSeason;
    @Element(name = "DVD_chapter", required = false)
    public String dvdChapter;
    @Element(name = "DVD_discid", required = false)
    public String dvdDiscId;
    @Element(name = "DVD_episodenumber", required = false)
    public String dvdEpisodeNumber;
    @Element(name = "DVD_season", required = false)
    public String dvdSeason;
    @Element(name = "Director", required = false)
    public String directors;
    @Element(name = "EpImgFlag", required = false)
    public String epImgFlag;
    @Element(name = "EpisodeName", required = false)
    public String episodeName;
    @Element(name = "EpisodeNumber", required = false)
    public int episodeNumber;
    @Element(name = "FirstAired", required = false)
    public String firstAired;
    @Element(name = "GuestStars", required = false)
    public String guestStars;
    @Element(name = "IMDB_ID", required = false)
    public String imdbId;
    @Element(name = "Language", required = false)
    public String language;
    @Element(name = "Overview", required = false)
    public String overview;
    @Element(name = "ProductionCode", required = false)
    public String productionCode;
    @Element(name = "Rating", required = false)
    public String rating;
    @Element(name = "SeasonNumber", required = false)
    public int seasonNumber;
    @Element(name = "Writer", required = false)
    public String writers;
    @Element(name = "absolute_number", required = false)
    public String absoluteNumber;
    @Element(name = "airsafter_season", required = false)
    public int airsAfterSeason;
    @Element(name = "airsbefore_episode", required = false)
    public int airsBeforeSeason;
    @Element(name = "airsbefore_season", required = false)
    public int airsBeforeEpisode;
    @Element(name = "filename", required = false)
    public String filename;
    @Element(name = "lastupdated", required = false)
    public String lastUpdated;
    @Element(name = "seasonid", required = false)
    public String seasonId;
    @Element(name = "seriesid", required = false)
    public String seriesId;

}
