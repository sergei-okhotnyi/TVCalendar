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

import java.util.ArrayList;
import java.util.List;

/**
 * @author matthew.altman
 */

public class Banners {

    private List<Banner> seriesList = new ArrayList<Banner>();
    private List<Banner> seasonList = new ArrayList<Banner>();
    private List<Banner> posterList = new ArrayList<Banner>();
    private List<Banner> fanartList = new ArrayList<Banner>();

    public List<Banner> getSeriesList() {
        return seriesList;
    }

    public List<Banner> getSeasonList() {
        return seasonList;
    }

    public List<Banner> getPosterList() {
        return posterList;
    }

    public List<Banner> getFanartList() {
        return fanartList;
    }

    void addSeriesBanner(Banner banner) {
        this.seriesList.add(banner);
    }

    void addSeasonBanner(Banner banner) {
        this.seasonList.add(banner);
    }

    void addPosterBanner(Banner banner) {
        this.posterList.add(banner);
    }

    void addFanartBanner(Banner banner) {
        this.fanartList.add(banner);
    }

    public void addBanner(Banner banner) {
        if (banner != null) {
            if (banner.getBannerType() == BannerListType.series) {
                addSeriesBanner(banner);
            } else if (banner.getBannerType() == BannerListType.season) {
                addSeasonBanner(banner);
            } else if (banner.getBannerType() == BannerListType.poster) {
                addPosterBanner(banner);
            } else if (banner.getBannerType() == BannerListType.fanart) {
                addFanartBanner(banner);
            }
        }
    }

    public boolean isEmpty() {
        return (seriesList == null || seriesList.isEmpty())
                && (seasonList == null || seasonList.isEmpty())
                && (posterList == null || posterList.isEmpty())
                && (fanartList == null || fanartList.isEmpty());
    }
}
