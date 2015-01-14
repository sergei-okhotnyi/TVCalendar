package com.uwetrottmann.trakt.v2.entities;

import java.io.Serializable;

public class SearchResult implements Serializable {

    public String type;
    public Double score;
    public Movie movie;
    public Show show;
    public Episode episode;
    public Person person;
    public List list;

}
