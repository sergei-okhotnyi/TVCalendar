/*
 *      Copyright (c) 2004-2013 Matthew Altman & Stuart Boston
 *
 *      This file is part of TheTVDB API.
 *
 *      TheTVDB API is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      any later version.
 *
 *      TheTVDB API is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with TheTVDB API.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package dev.okhotny.TVCalendar.providers.thetvdb;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.text.TextUtils;
import android.util.Xml;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.okhotny.TVCalendar.App;
import dev.okhotny.TVCalendar.FinalWrapper;
import dev.okhotny.TVCalendar.providers.SimpleCallback;
import dev.okhotny.TVCalendar.providers.model.Actor;
import dev.okhotny.TVCalendar.providers.model.Banner;
import dev.okhotny.TVCalendar.providers.model.BannerListType;
import dev.okhotny.TVCalendar.providers.model.BannerType;
import dev.okhotny.TVCalendar.providers.model.Banners;
import dev.okhotny.TVCalendar.providers.model.Episode;
import dev.okhotny.TVCalendar.providers.model.Series;

public class TheTVDBParser {

    private static final String TYPE_BANNER = "banner";
    private static final String TYPE_FANART = "fanart";
    private static final String TYPE_POSTER = "poster";
    private static final String BANNER_PATH = "BannerPath";
    private static final String VIGNETTE_PATH = "VignettePath";
    private static final String THUMBNAIL_PATH = "ThumbnailPath";

    // Hide the constructor
    private TheTVDBParser() {
    }

    /**
     * Get a list of the actors from the URL
     *
     * @param urlString
     * @param callback
     */
    public static void getActors(String urlString, final SimpleCallback<List<Actor>> callback) {

        App.sInstance.requestQueue.add(new StringRequest(urlString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final List<Actor> results = new ArrayList<Actor>();
                final FinalWrapper<Actor> actor = new FinalWrapper<Actor>();

                RootElement root = new RootElement("Actors");

                Element actorElement = root.getChild("Actor");

                actorElement.setStartElementListener(new StartElementListener() {
                    @Override
                    public void start(Attributes attributes) {
                        actor.value = new Actor();
                        results.add(actor.value);
                    }
                });

                actorElement.getChild("id").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        actor.value.setId(s);
                    }
                });
                actorElement.getChild("Image").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            actor.value.setImage(TheTVDBApi.GRAPHICS_BASE_URL + s);
                        }
                    }
                });
                actorElement.getChild("Name").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        actor.value.setName(s);
                    }
                });
                actorElement.getChild("Role").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        actor.value.setRole(s);
                    }
                });
                actorElement.getChild("SortOrder").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        actor.value.setSortOrder(s);
                    }
                });

                try {
                    Xml.parse(response, root.getContentHandler());
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                Collections.sort(results);
                callback.done(results);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.done(null);
            }
        }));

    }

    /**
     * Get all the episodes from the URL
     *
     * @param urlString
     * @param callback
     */
    public static void getAllData(String urlString, final SimpleCallback<Series> callback) {
        App.sInstance.requestQueue.add(new StringRequest(urlString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final Series results = new Series();
                final FinalWrapper<Episode> episode = new FinalWrapper<Episode>();

                RootElement root = new RootElement("Data");

                Element seriesElement = root.getChild("Series");

                seriesElement.getChild("id").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setId(s);
                    }
                });
                seriesElement.getChild("Actors").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setActors(s.substring(1, s.length() - 1));
                    }
                });
                seriesElement.getChild("Airs_DayOfWeek").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setAirsDayOfWeek(s);
                    }
                });
                seriesElement.getChild("Airs_Time").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setAirsTime(s);
                    }
                });
                seriesElement.getChild("ContentRating").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setContentRating(s);
                    }
                });
                seriesElement.getChild("FirstAired").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setFirstAired(s);
                    }
                });
                seriesElement.getChild("Genre").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setGenres(TextUtils.isEmpty(s) ? s : s.substring(1, s.length() - 1));
                    }
                });
                seriesElement.getChild("IMDB_ID").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setImdbId(s);
                    }
                });
                seriesElement.getChild("Network").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setNetwork(s);
                    }
                });
                seriesElement.getChild("Overview").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setOverview(s);
                    }
                });
                seriesElement.getChild("Rating").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setRating(s);
                    }
                });
                seriesElement.getChild("Runtime").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setRuntime(s);
                    }
                });
                seriesElement.getChild("SeriesName").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setSeriesName(s);
                    }
                });
                seriesElement.getChild("Status").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setStatus(s);
                    }
                });
                seriesElement.getChild(TYPE_BANNER).setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        if (!TextUtils.isEmpty(s))
                            results.setBanner(TheTVDBApi.GRAPHICS_BASE_URL + s);
                    }
                });
                seriesElement.getChild(TYPE_FANART).setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        if (!TextUtils.isEmpty(s))
                            results.setFanart(TheTVDBApi.GRAPHICS_BASE_URL + s);
                    }
                });

                seriesElement.getChild(TYPE_POSTER).setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        if (!TextUtils.isEmpty(s))
                            results.setPoster(TheTVDBApi.GRAPHICS_BASE_URL + s);
                    }
                });
                seriesElement.getChild("zap2it_id").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        results.setZap2ItId(s);
                    }
                });

                Element episodeElement = root.getChild("Episode");

                episodeElement.setStartElementListener(
                        new StartElementListener() {
                            @Override
                            public void start(Attributes attributes) {
                                episode.value = new Episode();
                                results.getEpisodes().add(episode.value);
                            }
                        }
                );

                episodeElement.getChild("id").setEndTextElementListener(
                        new EndTextElementListener() {
                            @Override
                            public void end(String s) {
                                episode.value.setId(s);
                            }
                        }
                );
                episodeElement.getChild("EpisodeName").setEndTextElementListener(
                        new EndTextElementListener() {
                            @Override
                            public void end(String s) {
                                episode.value.setEpisodeName(s);
                            }
                        }
                );
                episodeElement.getChild("EpisodeNumber").setEndTextElementListener(
                        new EndTextElementListener() {
                            @Override
                            public void end(String s) {
                                episode.value.setEpisodeNumber(s);
                            }
                        }
                );
                episodeElement.getChild("FirstAired").setEndTextElementListener(
                        new EndTextElementListener() {
                            @Override
                            public void end(String s) {
                                episode.value.setFirstAired(s);
                            }
                        }
                );
                episodeElement.getChild("IMDB_ID").setEndTextElementListener(
                        new EndTextElementListener() {
                            @Override
                            public void end(String s) {
                                episode.value.setImdbId(s);
                            }
                        }
                );
                episodeElement.getChild("Overview").setEndTextElementListener(
                        new EndTextElementListener() {
                            @Override
                            public void end(String s) {
                                episode.value.setOverview(s);
                            }
                        }
                );
                episodeElement.getChild("Rating").setEndTextElementListener(
                        new EndTextElementListener() {
                            @Override
                            public void end(String s) {
                                episode.value.setRating(s);
                            }
                        }
                );
                episodeElement.getChild("SeasonNumber").setEndTextElementListener(
                        new EndTextElementListener() {
                            @Override
                            public void end(String s) {
                                episode.value.setSeasonNumber(s);
                            }
                        }
                );
                episodeElement.getChild("filename").setEndTextElementListener(
                        new EndTextElementListener() {
                            @Override
                            public void end(String s) {
                                if (!TextUtils.isEmpty(s)) {
                                    episode.value.setFilename(TheTVDBApi.GRAPHICS_BASE_URL + s);
                                }
                            }
                        }
                );
                episodeElement.getChild("seriesid").setEndTextElementListener(
                        new EndTextElementListener() {
                            @Override
                            public void end(String s) {
                                episode.value.setSeriesId(s);
                            }
                        }
                );

                try {
                    Xml.parse(response, root.getContentHandler());
                } catch (SAXException e) {
                    e.printStackTrace();
                }

                callback.done(results);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.done(null);
            }
        }));
    }

    /**
     * Get a list of banners from the URL
     *
     * @param urlString
     * @param callback
     */
    public static void getBanners(String urlString, final SimpleCallback<Banners> callback) {

        App.sInstance.requestQueue.add(new StringRequest(urlString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final Banners results = new Banners();
                final FinalWrapper<Banner> episode = new FinalWrapper<Banner>();

                RootElement root = new RootElement("Banners");

                Element episodeElement = root.getChild("Banner");

                episodeElement.setStartElementListener(new StartElementListener() {
                    @Override
                    public void start(Attributes attributes) {
                        episode.value = new Banner();
                    }
                });

                episodeElement.setEndElementListener(new EndElementListener() {
                    @Override
                    public void end() {
                        results.addBanner(episode.value);
                    }
                });

                episodeElement.getChild(BANNER_PATH).setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            episode.value.setUrl(TheTVDBApi.GRAPHICS_BASE_URL + s);
                        }
                    }
                });
                episodeElement.getChild(VIGNETTE_PATH).setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            episode.value.setVignette(TheTVDBApi.GRAPHICS_BASE_URL + s);
                        }
                    }
                });
                episodeElement.getChild(THUMBNAIL_PATH).setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            episode.value.setThumb(TheTVDBApi.GRAPHICS_BASE_URL + s);
                        }
                    }
                });

                episodeElement.getChild("id").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setId(s);
                    }
                });

                episodeElement.getChild("BannerType").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setBannerType(BannerListType.fromString(s));
                    }
                });
                episodeElement.getChild("BannerType2").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setBannerType2(BannerType.fromString(s));
                    }
                });
                episodeElement.getChild("Language").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setLanguage(s);
                    }
                });
                episodeElement.getChild("Season").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setSeason(s);
                    }
                });
                episodeElement.getChild("Colors").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setColours(s);
                    }
                });
                episodeElement.getChild("Rating").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setRating(s);
                    }
                });
                episodeElement.getChild("RatingCount").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setRatingCount(s);
                    }
                });
                episodeElement.getChild("SeriesName").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setSeriesName(s);
                    }
                });

                try {
                    Xml.parse(response, root.getContentHandler());
                } catch (SAXException e) {
                    e.printStackTrace();
                }
                callback.done(results);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.done(null);
            }
        }));
    }

    /**
     * Get a list of series from the URL
     *
     * @param urlString
     * @param callback
     */
    public static void getSeriesList(String urlString, final SimpleCallback<List<Series>> callback) {
        final List<Series> results = new ArrayList<Series>();
        final FinalWrapper<Series> episode = new FinalWrapper<Series>();

        App.sInstance.requestQueue.add(new StringRequest(urlString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                RootElement root = new RootElement("Data");

                Element episodeElement = root.getChild("Series");

                episodeElement.setStartElementListener(new StartElementListener() {
                    @Override
                    public void start(Attributes attributes) {
                        episode.value = new Series();
                        results.add(episode.value);
                    }
                });
                episodeElement.getChild("id").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setId(s);
                    }
                });
                episodeElement.getChild("Actors").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setActors(s.substring(1, s.length() - 1));
                    }
                });
                episodeElement.getChild("Airs_DayOfWeek").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setAirsDayOfWeek(s);
                    }
                });
                episodeElement.getChild("Airs_Time").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setAirsTime(s);
                    }
                });
                episodeElement.getChild("ContentRating").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setContentRating(s);
                    }
                });
                episodeElement.getChild("FirstAired").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setFirstAired(s);
                    }
                });
                episodeElement.getChild("Genre").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setGenres(TextUtils.isEmpty(s) ? s : s.substring(1, s.length() - 1));
                    }
                });
                episodeElement.getChild("IMDB_ID").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setImdbId(s);
                    }
                });
                episodeElement.getChild("Network").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setNetwork(s);
                    }
                });
                episodeElement.getChild("Overview").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setOverview(s);
                    }
                });
                episodeElement.getChild("Rating").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setRating(s);
                    }
                });
                episodeElement.getChild("Runtime").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setRuntime(s);
                    }
                });
                episodeElement.getChild("SeriesName").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setSeriesName(s);
                    }
                });
                episodeElement.getChild("Status").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setStatus(s);
                    }
                });
                episodeElement.getChild(TYPE_BANNER).setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        if (!TextUtils.isEmpty(s))
                            episode.value.setBanner(TheTVDBApi.GRAPHICS_BASE_URL + s);
                    }
                });
                episodeElement.getChild(TYPE_FANART).setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        if (!TextUtils.isEmpty(s))
                            episode.value.setFanart(TheTVDBApi.GRAPHICS_BASE_URL + s);
                    }
                });

                episodeElement.getChild(TYPE_POSTER).setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        if (!TextUtils.isEmpty(s))
                            episode.value.setPoster(TheTVDBApi.GRAPHICS_BASE_URL + s);
                    }
                });
                episodeElement.getChild("zap2it_id").setEndTextElementListener(new EndTextElementListener() {
                    @Override
                    public void end(String s) {
                        episode.value.setZap2ItId(s);
                    }
                });

                try {
                    Xml.parse(response, root.getContentHandler());
                } catch (SAXException e) {
                    e.printStackTrace();
                }

                callback.done(results);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                callback.done(null);
            }
        }));
    }

}
