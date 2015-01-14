package com.uwetrottmann.trakt.v2.entities;

import java.io.Serializable;
import java.util.List;

public class BaseSeason implements Serializable {

    public Integer number;
    public List<BaseEpisode> episodes;

}
