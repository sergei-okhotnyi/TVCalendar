package com.uwetrottmann.trakt.v2.entities;

import org.joda.time.DateTime;

import java.io.Serializable;

public class BaseEpisode implements Serializable {

    public Integer number;

    public DateTime collected_at;
    public int plays;

}
