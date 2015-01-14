package com.uwetrottmann.trakt.v2.entities;

import org.joda.time.DateTime;

import java.io.Serializable;

public class WatchlistedEpisode implements Serializable {

    public DateTime listed_at;

    public Episode episode;
    public Show show;

}
