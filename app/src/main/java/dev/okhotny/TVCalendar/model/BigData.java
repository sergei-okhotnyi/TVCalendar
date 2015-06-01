package dev.okhotny.TVCalendar.model;


import android.util.SparseArray;

import com.jakewharton.trakt.entities.TvShow;
import com.jakewharton.trakt.entities.TvShowProgress;
import com.jakewharton.trakt.services.ShowService;

import java.io.Serializable;
import java.util.List;

public class BigData implements Serializable {
    private static final long serialVersionUID = 1L;

    public long lastSync;

    public TvShow traktv;

    public List<TvShowProgress.Season> seasons;

    public SparseArray<List<ShowService.Episodes.Episode>> episodes;

}
