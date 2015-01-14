package com.uwetrottmann.trakt.v2.entities;

import java.io.Serializable;

public class Season implements Serializable {

    public Integer number;
    public SeasonIds ids;

    public Double rating;
    public Integer episode_count;
    public Images images;

}
