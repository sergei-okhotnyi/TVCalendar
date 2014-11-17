package dev.okhotny.TVCalendar.model;

import java.io.Serializable;

public class TvDbSeason implements Serializable {
    private static final long serialVersionUID = 1L;

    public int seasonNumber;

    public TvDbSeason(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }
}
