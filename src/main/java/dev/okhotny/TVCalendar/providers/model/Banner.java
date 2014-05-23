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
package dev.okhotny.TVCalendar.providers.model;

/**
 * @author altman.matthew
 */

public class Banner {

    private int id;
    private String url;
    private BannerListType bannerType;
    private BannerType bannerType2;
    private String colours;
    private Float rating;
    private int ratingCount;
    private String language;
    private boolean seriesName;
    private String thumb;
    private String vignette;
    private int season = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(String id) {
        try {
            this.id = Integer.parseInt(id);
        } catch (Exception ignore) {
            this.id = 0;
        }
    }

    public BannerListType getBannerType() {
        return bannerType;
    }

    public void setBannerType(BannerListType bannerType) {
        this.bannerType = bannerType;
    }

    public BannerType getBannerType2() {
        return bannerType2;
    }

    public void setBannerType2(BannerType bannerType2) {
        this.bannerType2 = bannerType2;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(String rating) {
        try {
            this.rating = Float.parseFloat(rating);
        } catch (Exception ignore) {
            this.rating = 0f;
        }
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(String ratingCount) {
        try {
            this.ratingCount = Integer.parseInt(ratingCount);
        } catch (Exception ignore) {
            this.ratingCount = 0;
        }
    }

    public String getColours() {
        return colours;
    }

    public void setColours(String colours) {
        this.colours = colours;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setSeason(String season) {
        try {
            this.season = Integer.parseInt(season);
        } catch (Exception error) {
            this.season = 0;
        }
    }

    public boolean isSeriesName() {
        return seriesName;
    }

    public void setSeriesName(boolean seriesName) {
        this.seriesName = seriesName;
    }

    public void setSeriesName(String seriesName) {
        try {
            this.seriesName = Boolean.parseBoolean(seriesName);
        } catch (Exception ignore) {
            this.seriesName = false;
        }

    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVignette() {
        return vignette;
    }

    public void setVignette(String vignette) {
        this.vignette = vignette;
    }

}
