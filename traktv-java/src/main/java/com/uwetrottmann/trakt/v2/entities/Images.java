package com.uwetrottmann.trakt.v2.entities;

import java.io.Serializable;

public class Images implements Serializable {

    public MoreImageSizes poster;
    public MoreImageSizes fanart;
    public MoreImageSizes screenshot;
    /**
     * A {@link com.uwetrottmann.trakt.v2.entities.Person} has headshots.
     */
    public MoreImageSizes headshot;
    public ImageSizes banner;
    public ImageSizes logo;
    public ImageSizes clearart;
    public ImageSizes thumb;
    public ImageSizes avatar;

}
