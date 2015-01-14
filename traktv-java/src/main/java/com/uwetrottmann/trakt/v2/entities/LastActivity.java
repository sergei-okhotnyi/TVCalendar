package com.uwetrottmann.trakt.v2.entities;

import org.joda.time.DateTime;

import java.io.Serializable;

public class LastActivity implements Serializable {

    public DateTime rated_at;
    public DateTime watchlisted_at;
    public DateTime commented_at;

}
