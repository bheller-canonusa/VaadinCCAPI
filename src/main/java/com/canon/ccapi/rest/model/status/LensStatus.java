package com.canon.ccapi.rest.model.status;

import com.canon.ccapi.rest.interfaces.*;

@ResponseType(type = ResponseTypes.JSON)
@RestCommand(restcommand = "devicestatus/lens",restverb = RestVerbs.GET)
public class LensStatus implements CCAPIPojos {

    private Boolean mount;
    private String name;

    public LensStatus(){}

    public LensStatus(Boolean mount, String name) {
        this.mount = mount;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Lens{" +
                "mount=" + mount +
                ", name='" + name + '\'' +
                '}';
    }

    public Boolean getMount() {
        return mount;
    }

    public void setMount(Boolean mount) {
        this.mount = mount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
