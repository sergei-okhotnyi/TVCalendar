package com.uwetrottmann.trakt.v2.entities;

import com.uwetrottmann.trakt.v2.enums.Rating;

import org.joda.time.DateTime;

import java.io.Serializable;

public class BaseRatedEntity implements Serializable {

    public DateTime rated_at;
    public Rating rating;

}
