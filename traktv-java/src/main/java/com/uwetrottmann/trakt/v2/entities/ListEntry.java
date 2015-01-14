package com.uwetrottmann.trakt.v2.entities;

import org.joda.time.DateTime;

import java.io.Serializable;

public class ListEntry implements Serializable {

    public DateTime listed_at;
    public Movie movie;
    public Show show;
    public Episode episode;
    public Person person;

}
