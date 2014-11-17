package dev.okhotny.TVCalendar.model;

import com.jakewharton.trakt.entities.TvShow;

import java.io.Serializable;

public class BigData implements Serializable {
    private static final long serialVersionUID = 1L;

    public TvShow traktv;
    public TvDbSeries thetvdb;
}
