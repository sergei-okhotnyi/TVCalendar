package com.uwetrottmann.trakt.v2.entities;

import java.io.Serializable;
import java.util.List;

public class SyncErrors implements Serializable {

    public List<SyncMovie> movies;
    public List<SyncShow> shows;
    public List<SyncSeason> seasons;
    public List<SyncEpisode> episodes;

}
