package com.omertron.thetvdbapi.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by sergiio on 11/13/2014.
 */
@Root(strict = false)
public class Data {

    @ElementList(entry = "Series", inline = true, required = false)
    List<Series> Series;

    @ElementList(entry = "Episode", inline = true, required = false)
    List<Episode> Episode;
}
