package com.uwetrottmann.trakt.v2.entities;

import org.joda.time.DateTime;

import java.io.Serializable;

public abstract class BaseCheckinResponse implements Serializable {

    public DateTime watched_at;
    public ShareSettings sharing;

}
