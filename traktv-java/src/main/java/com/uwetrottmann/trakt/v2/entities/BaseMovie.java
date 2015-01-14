package com.uwetrottmann.trakt.v2.entities;

import org.joda.time.DateTime;

import java.io.Serializable;

public class BaseMovie implements Serializable {

    public Movie movie;

    public DateTime collected_at;
    public DateTime listed_at;
    public int plays;

}
