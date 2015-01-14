package com.uwetrottmann.trakt.v2.entities;

import org.joda.time.DateTime;

import java.io.Serializable;

public class HistoryEntry implements Serializable {

    public DateTime watched_at;
    public String action;

    public Episode episode;
    public Show show;

    public Movie movie;

}
