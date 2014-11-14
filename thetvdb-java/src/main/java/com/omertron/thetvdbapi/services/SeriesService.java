package com.omertron.thetvdbapi.services;

import com.omertron.thetvdbapi.model.Actor;
import com.omertron.thetvdbapi.model.Banners;
import com.omertron.thetvdbapi.model.Data;
import com.omertron.thetvdbapi.model.Series;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by sergiio on 11/13/2014.
 */
public interface SeriesService {

    @GET("/{apikey}/series/{seriesid}/")
    Data get(
            @Path("seriesid") int tvdbId
    );

    @GET("/{apikey}/series/{seriesid}/all/")
    Data all(
            @Path("seriesid") int tvdbId
    );

    @GET("/{apikey}/series/{seriesid}/banners.xml")
    Banners banners(
            @Path("seriesid") int tvdbId
    );

    @GET("/{apikey}/series/{seriesid}/actors.xml")
    List<Actor> actors(
            @Path("seriesid") int tvdbId
    );

}
