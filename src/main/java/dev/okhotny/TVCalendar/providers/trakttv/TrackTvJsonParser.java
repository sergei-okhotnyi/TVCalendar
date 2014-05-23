package dev.okhotny.TVCalendar.providers.trakttv;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dev.okhotny.TVCalendar.App;
import dev.okhotny.TVCalendar.providers.SimpleCallback;
import dev.okhotny.TVCalendar.providers.model.Comment;
import dev.okhotny.TVCalendar.providers.model.Series;

public class TrackTvJsonParser {
    public static void getSeriesList(String url, final SimpleCallback<List<Series>> simpleCallback) {
        App.sInstance.requestQueue.add(new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Series> result = new ArrayList<Series>(response.length());
                for (int i = 0; i < response.length(); i++) {
                    JSONObject json = response.optJSONObject(i);
                    Series series = new Series();
                    series.setSeriesName(json.optString("title"));
                    series.setOverview(json.optString("overview"));
                    series.setId(json.optString("tvdb_id"));
                    series.setImdbId(json.optString("tvdb_id"));
                    series.setStatus(json.optString("status"));
                    series.setAirsTime(json.optString("air_time"));
                    series.setAirsDayOfWeek(json.optString("air_day"));
                    try {
                        series.setFirstAired(new Date(json.optLong("first_aired")).toString());
                    } catch (Exception ignored) {
                    }
                    result.add(series);
                }
                simpleCallback.done(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                simpleCallback.done(null);
            }
        }));
    }

    public static void getSeriesComments(String url, final SimpleCallback<List<Comment>> callback) {
        App.sInstance.requestQueue.add(new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                List<Comment> result = new ArrayList<Comment>(response.length());
                for (int i = 0; i < response.length(); i++) {
                    JSONObject json = response.optJSONObject(i);
                    Comment comment = new Comment();
                    comment.text = json.optString("text");
                    comment.inserted = json.optString("inserted");
                    JSONObject user = json.optJSONObject("user");
                    comment.avatar = user.optString("avatar");
                    comment.username = user.optString("username");
                    result.add(comment);
                }
                callback.done(result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.done(null);
            }
        }));
    }
}
