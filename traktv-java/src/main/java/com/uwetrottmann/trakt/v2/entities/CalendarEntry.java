package com.uwetrottmann.trakt.v2.entities;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Calendar entry with either {@code airs_at, episode, show} filled or only {@code movie}.
 */
public class CalendarEntry implements Serializable {

    public DateTime airs_at;
    public Episode episode;
    public Show show;
    public Movie movie;

}
