package com.uwetrottmann.trakt.v2.entities;

import java.io.Serializable;

public class CrewMember implements Serializable {

    public String job;
    public Movie movie;
    public Show show;
    public Person person;

}
