package com.uwetrottmann.trakt.v2.entities;

import java.io.Serializable;

public class SyncResponse implements Serializable {

    public SyncStats added;
    public SyncStats existing;
    public SyncStats deleted;
    public SyncErrors not_found;

}
