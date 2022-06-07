package com.canon.ccapi.rest.model.storage;

import com.canon.ccapi.rest.interfaces.*;

@ResponseType(type = ResponseTypes.JSON)
@RestCommand(restcommand = "contents/{?}",restverb = RestVerbs.GET)
public class StorageName implements CCAPIPojos {

    private String[] url;

    public StorageName(){}

    public StorageName(String[] url) {
        this.url = url;
    }

    public String[] getUrl() {
        return url;
    }

    public void setUrl(String[] url) {
        this.url = url;
    }
}
