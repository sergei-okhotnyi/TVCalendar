package dev.okhotny.TVCalendar.database;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.okhotny.TVCalendar.App;

public class AgendaModel {
    @DatabaseField(id = true)
    private String episodeId;
    @DatabaseField(dataType = DataType.DATE_LONG)
    private Date firstAired;
    @DatabaseField
    private String episodeName;
    @DatabaseField
    private String seasonNumber;
    @DatabaseField
    private String episodeNumber;
    @DatabaseField
    private String seriesId;
    @DatabaseField
    private String seriesName;
    @DatabaseField
    private String airsTime;
    @DatabaseField
    private String network;
    @DatabaseField
    private boolean isWatched;
    @DatabaseField
    private String filename;
    @DatabaseField
    private String overview;
    @DatabaseField
    private String poster;

    public static List<AgendaModel> getAgenda(boolean hide_watched) {
        List<AgendaModel> result = new ArrayList<AgendaModel>();
        DatabaseHelper helper = OpenHelperManager.getHelper(App.sInstance, DatabaseHelper.class);
        try {
            Dao<AgendaModel, String> agendaDao = helper.getAgendaDao();

            GenericRawResults<AgendaModel> agendaModels = agendaDao.queryRaw("select " +
                            // "Episode.id episodeId, Episode.firstAired, Episode.episodeName, Episode.seasonNumber, Episode.episodeNumber, Episode.isWatched, Episode.filename, Episode.overview, " +
                            "Series.id seriesId, Series.seriesName, Series.airsTime, Series.network, Series.poster " +
                            "from " +
                            //"Episode, " +
                            "Series ", //+
                    //"where Episode.seriesId=Series.id and Episode.firstAired is not null " +
                    //(hide_watched ? "and Episode.isWatched=0 " : "") +
                    //"order by Episode.firstAired desc",
                    agendaDao.getRawRowMapper()
            );

            for (AgendaModel a : agendaModels) {
                result.add(a);
            }

            agendaModels.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return result;
    }

    public Date getFirstAired() {
        return firstAired;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public String getSeasonNumber() {
        return seasonNumber;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public String getAirsTime() {
        return airsTime;
    }

    public String getNetwork() {
        return network;
    }

    public String getEpisodeNumber() {
        return episodeNumber;
    }

    public void setIsWatched(boolean watched) {
        isWatched = watched;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    public String getFileName() {
        return filename;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster() {
        return poster;
    }
}
