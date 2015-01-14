package com.uwetrottmann.trakt.v2.entities;

import java.io.Serializable;
import java.util.Map;

public class Ratings implements Serializable {

    public Double rating;
    public Integer votes;
    public Map<String, Integer> distribution;

}
