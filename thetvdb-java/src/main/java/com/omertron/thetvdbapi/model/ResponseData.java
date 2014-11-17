package com.omertron.thetvdbapi.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class ResponseData {

    @ElementList(entry = "Series", inline = true, required = false)
    public List<Series> serieses;

    @ElementList(entry = "Episode", inline = true, required = false)
    public List<Episode> episodes;
}
