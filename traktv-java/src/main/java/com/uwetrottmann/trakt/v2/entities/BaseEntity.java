package com.uwetrottmann.trakt.v2.entities;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

public abstract class BaseEntity implements Serializable {

    public String title;
    public DateTime updated_at;
    public Images images;
    public List<String> available_translations;

}
