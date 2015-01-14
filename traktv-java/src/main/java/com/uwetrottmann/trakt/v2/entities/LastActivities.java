package com.uwetrottmann.trakt.v2.entities;

import java.io.Serializable;

public class LastActivities implements Serializable {

    public LastActivityMore movies;
    public LastActivityMore episodes;
    public LastActivity shows;
    public LastActivity seasons;

}
