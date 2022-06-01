package com.canon.ccapi.rest.model.shootingcontrol;

import com.canon.ccapi.rest.interfaces.*;
import com.fasterxml.jackson.annotation.JsonProperty;


@ResponseType(type = ResponseTypes.JSON)
@RestCommand(restcommand = "shooting/control/shutterbutton",restverb = RestVerbs.POST)
public class StillImageCapture implements CCAPIPojos {

    @PossiblePostValues(possiblevalues = {"true","false"})
    @PossiblePostValuesForUI(possiblevalues = {"true","false"})
    @JsonProperty
    private Boolean af;

    public StillImageCapture(){}

    public StillImageCapture(Boolean af) {
        this.af = af;
    }

    public Boolean getAf() {
        return af;
    }

    public void setAf(Boolean af) {
        this.af = af;
    }

    @Override
    public String toString() {
        return "StillImageCapture{" +
                "af=" + af +
                '}';
    }
}
