package dev.okhotny.TVCalendar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import dev.okhotny.TVCalendar.providers.model.Episode;
import dev.okhotny.TVCalendar.providers.model.Series;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "main.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Episode, String> episodeDao = null;
    private Dao<Series, String> seriesDao = null;
    private Dao<AgendaModel, String> agendaDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Episode.class);
            TableUtils.createTable(connectionSource, Series.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }

    public Dao<Episode, String> getEpisodeDao() throws SQLException {
        if (episodeDao == null) {
            episodeDao = getDao(Episode.class);
        }
        return episodeDao;
    }

    public Dao<Series, String> getSeriesDao() throws SQLException {
        if (seriesDao == null) {
            seriesDao = getDao(Series.class);
        }
        return seriesDao;
    }

    public Dao<AgendaModel, String> getAgendaDao() throws SQLException {
        if (agendaDao == null) {
            agendaDao = getDao(AgendaModel.class);
        }
        return agendaDao;
    }
}
