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
package dev.okhotny.TVCalendar.providers.thetvdb;

import android.preference.PreferenceManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import dev.okhotny.TVCalendar.App;
import dev.okhotny.TVCalendar.R;
import dev.okhotny.TVCalendar.providers.SimpleCallback;
import dev.okhotny.TVCalendar.providers.model.Actor;
import dev.okhotny.TVCalendar.providers.model.Banners;
import dev.okhotny.TVCalendar.providers.model.Episode;
import dev.okhotny.TVCalendar.providers.model.Series;

/**
 * @author altman.matthew
 * @author stuart.boston
 */
public class TheTVDBApi {

    public static final String GRAPHICS_BASE_URL = "http://thetvdb.com/banners/";
    private static final String XML_EXTENSION = ".xml";
    private static final String SERIES_URL = "/series/";
    private static final String ALL_URL = "/all/";
    private static final String API_BASE_URL = "http://thetvdb.com/api/";
    private static final String sApiKey;
    private static String language;

    static {
        sApiKey = App.sInstance.getString(R.string.thetvdb_apikey);
        updatePreferredLanguage();
    }

    public static void updatePreferredLanguage() {
        language = PreferenceManager.getDefaultSharedPreferences(App.sInstance).getString("preferred_language", null);
    }


    /**
     * Get the series information
     */
    public static void getSeries(String id, boolean noCache, final SimpleCallback<Series> callback) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(API_BASE_URL);
        urlBuilder.append(sApiKey);
        urlBuilder.append(SERIES_URL);
        urlBuilder.append(id);
        urlBuilder.append("/");
        if (language != null) {
            urlBuilder.append(language).append(XML_EXTENSION);
        }
        String url = urlBuilder.toString();
        TheTVDBParser.getSeriesList(url, new SimpleCallback<List<Series>>() {
            @Override
            public void done(List<Series> result) {
                if (result != null && !result.isEmpty()) {
                    callback.done(result.get(0));
                } else {
                    callback.done(null);
                }
            }
        });
    }

    /**
     * Get all the episodes for a series. Note: This could be a lot of records
     */
    public static void getAllEpisodes(String id, boolean noCache, SimpleCallback<List<Episode>> callback) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(API_BASE_URL);
        urlBuilder.append(sApiKey);
        urlBuilder.append(SERIES_URL);
        urlBuilder.append(id);
        urlBuilder.append(ALL_URL);
        if (language != null) {
            urlBuilder.append(language).append(XML_EXTENSION);
        }
        String url = urlBuilder.toString();
        TheTVDBParser.getAllEpisodes(url, callback);
    }

    public static void getBanners(String seriesId, SimpleCallback<Banners> callback) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(API_BASE_URL);
        urlBuilder.append(sApiKey);
        urlBuilder.append(SERIES_URL);
        urlBuilder.append(seriesId);
        urlBuilder.append("/banners.xml");

        TheTVDBParser.getBanners(urlBuilder.toString(), callback);
    }

    /**
     * Get a list of actors from the series id
     */
    public static void getActors(String seriesId, SimpleCallback<List<Actor>> callback) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(API_BASE_URL);
        urlBuilder.append(sApiKey);
        urlBuilder.append(SERIES_URL);
        urlBuilder.append(seriesId);
        urlBuilder.append("/actors.xml");
        TheTVDBParser.getActors(urlBuilder.toString(), callback);
    }

    public static void searchSeries(String title, SimpleCallback<List<Series>> callback) {
        StringBuilder urlBuilder = new StringBuilder();

        urlBuilder.append(API_BASE_URL);
        urlBuilder.append("GetSeries.php?seriesname=");
        try {
            urlBuilder.append(URLEncoder.encode(title, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            callback.done(new ArrayList<Series>(0));
            return;
        }
        if (language != null) {
            urlBuilder.append("&language=").append(language);
        }
        TheTVDBParser.getSeriesList(urlBuilder.toString(), callback);
    }

}
