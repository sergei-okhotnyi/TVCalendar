package dev.okhotny.TVCalendar.model;


import android.util.SparseArray;

import com.uwetrottmann.trakt.v2.entities.Episode;
import com.uwetrottmann.trakt.v2.entities.Season;
import com.uwetrottmann.trakt.v2.entities.Show;

import java.io.Serializable;
import java.util.List;

public class BigData implements Serializable {
    private static final long serialVersionUID = 1L;

    public long lastSync;

    public Show traktv;

    public List<Season> seasons;

    public SparseArray<List<Episode>> episodes;

}
