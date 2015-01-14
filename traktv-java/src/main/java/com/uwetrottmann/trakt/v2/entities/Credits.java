package com.uwetrottmann.trakt.v2.entities;

import java.io.Serializable;
import java.util.List;

public class Credits implements Serializable {

    public List<CastMember> cast;
    public Crew crew;

}
