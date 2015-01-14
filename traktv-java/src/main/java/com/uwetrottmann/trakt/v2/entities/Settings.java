package com.uwetrottmann.trakt.v2.entities;

import java.io.Serializable;

public class Settings implements Serializable {

    public User user;
    public Account account;
    public Connections connections;
    public SharingText sharing_text;

}
