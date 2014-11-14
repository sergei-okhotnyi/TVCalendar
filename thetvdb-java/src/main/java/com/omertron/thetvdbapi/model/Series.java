/*
 *      Copyright (c) 2004-2014 Matthew Altman & Stuart Boston
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
package com.omertron.thetvdbapi.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author altman.matthew
 */
@Root(strict = false)
public class Series implements Serializable {

    // Default serial UID
    private static final long serialVersionUID = 1L;
    @Element(name = "id", required = false)
    public String id;
    @Element(name = "SeriesID", required = false)
    public String seriesID;
    @Element(name = "Language", required = false)
    public String language;
    @Element(name = "SeriesName", required = false)
    public String seriesName;
    @Element(name = "Overview", required = false)
    public String overview;
    @Element(name = "FirstAired", required = false)
    public String firstAired;
    @Element(name = "IMDB_ID", required = false)
    public String imdbId;
    @Element(name = "zap2it_id", required = false)
    public String zap2ItId;
    @Element(name = "Actors", required = false)
    public String actors;
    @Element(name = "Airs_DayOfWeek", required = false)
    public String airsDayOfWeek;
    @Element(name = "Airs_Time", required = false)
    public String airsTime;
    @Element(name = "ContentRating", required = false)
    public String contentRating;
    @Element(name = "Genre", required = false)
    public String genres;
    @Element(name = "Network", required = false)
    public String network;
    @Element(name = "Rating", required = false)
    public String rating;
    @Element(name = "Runtime", required = false)
    public String runtime;
    @Element(name = "Status", required = false)
    public String status;
    @Element(name = "banner", required = false)
    public String banner;
    @Element(name = "fanart", required = false)
    public String fanart;
    @Element(name = "poster", required = false)
    public String poster;
    @Element(name = "lastupdated", required = false)
    public String lastUpdated;


}
