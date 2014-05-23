package dev.okhotny.TVCalendar.providers.trakttv;

import java.util.List;

import dev.okhotny.TVCalendar.App;
import dev.okhotny.TVCalendar.R;
import dev.okhotny.TVCalendar.providers.SimpleCallback;
import dev.okhotny.TVCalendar.providers.model.Comment;
import dev.okhotny.TVCalendar.providers.model.Series;

public class TraktTvApi {

    private static final String sApiKey;

    static {
        sApiKey = App.sInstance.getString(R.string.traktv_apikey);
    }

    private static final String API_BASE_URL = "http://api.trakt.tv";
    private static final String JSON_EXTENSION = ".json";


    public static void getTrendingSeries(final SimpleCallback<List<Series>> callback) {
        String url = API_BASE_URL + "/shows" + "/trending" + JSON_EXTENSION + "/" + sApiKey;
        TrackTvJsonParser.getSeriesList(url, callback);
    }

    public static void getSeriesReviews(final String id, final SimpleCallback<List<Comment>> callback) {
        String url = new StringBuilder().append(API_BASE_URL).append("/show").append("/comments").append(JSON_EXTENSION).append("/").append(sApiKey).append("/").append(id).append("/reviews").toString();
        TrackTvJsonParser.getSeriesComments(url, callback);
    }

    public static void getSeriesShouts(final String id, final SimpleCallback<List<Comment>> callback) {
        String url = new StringBuilder().append(API_BASE_URL).append("/show").append("/comments").append(JSON_EXTENSION).append("/").append(sApiKey).append("/").append(id).append("/shouts").toString();
        TrackTvJsonParser.getSeriesComments(url, callback);
    }

}
