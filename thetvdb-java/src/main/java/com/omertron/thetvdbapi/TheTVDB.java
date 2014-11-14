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
package com.omertron.thetvdbapi;

import com.mobprofs.retrofit.converters.SimpleXmlConverter;
import com.omertron.thetvdbapi.services.SeriesService;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * @author altman.matthew
 * @author stuart.boston
 */
public class TheTVDB {

    public static final String PARAM_API_KEY = "apikey";
    private static final String API_URL = "http://thetvdb.com/api/";
    private static final String BANNER_URL = "http://thetvdb.com/banners/";
    private String apiKey = null;
    private boolean isDebug;
    private RestAdapter restAdapter;

    /**
     * Create an API object with the passed API Key
     */
    public TheTVDB() {
    }

    /**
     * Set your TheTVDB api key
     */
    public TheTVDB setApiKey(String key) {
        apiKey = key;
        restAdapter = null;
        return this;
    }

    /**
     * Set the {@link retrofit.RestAdapter} log level.
     *
     * @param isDebug If true, the log level is set to {@link retrofit.RestAdapter.LogLevel#FULL}.
     *                Otherwise {@link retrofit.RestAdapter.LogLevel#NONE}.
     */
    public TheTVDB setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        if (restAdapter != null) {
            restAdapter.setLogLevel(isDebug ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE);
        }
        return this;
    }

    /**
     * Create a new {@link retrofit.RestAdapter.Builder}. Override this to e.g. set your own client or executor.
     *
     * @return A {@link retrofit.RestAdapter.Builder} with no modifications.
     */
    protected RestAdapter.Builder newRestAdapterBuilder() {
        return new RestAdapter.Builder();
    }


    /**
     * Return the current {@link retrofit.RestAdapter} instance. If none exists (first call, API key changed),
     * builds a new one.
     * <p/>
     * When building, sets the endpoint, a custom converter ({@link TraktHelper#getGsonBuilder()})
     * and a {@link retrofit.RequestInterceptor} which adds the API key as path param and if available adds
     * authentication to the request header.
     */
    protected RestAdapter getRestAdapter() {
        if (restAdapter == null) {
            RestAdapter.Builder builder = newRestAdapterBuilder();

            builder.setEndpoint(API_URL);
            builder.setConverter(new SimpleXmlConverter());
            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade requestFacade) {
                    requestFacade.addPathParam(PARAM_API_KEY, apiKey);
                }
            });

            if (isDebug) {
                builder.setLogLevel(RestAdapter.LogLevel.FULL);
            }

            restAdapter = builder.build();
        }

        return restAdapter;
    }

    public SeriesService seriesService() {
        return getRestAdapter().create(SeriesService.class);
    }

}
