package dev.okhotny.TVCalendar.providers.model;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class Season {
    public final List<Episode> EpisodeList = new ArrayList<Episode>();
    public int SeasonNumber;

    public static SparseArray<Season> getSeasons(List<Episode> episodes) {
        SparseArray<Season> result = new SparseArray<Season>();
        for (Episode e : episodes) {
            Season s = result.get(e.getSeasonNumber());
            if (s == null) {
                s = new Season();
                s.SeasonNumber = e.getSeasonNumber();
                result.put(e.getSeasonNumber(), s);
            }
            s.EpisodeList.add(e);
        }
        return result;
    }
}
