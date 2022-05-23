package com.canon.ccapi.rest.model.liveview;

import com.canon.ccapi.rest.interfaces.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
@JsonProperty are the properties that are part of the POST request, not the response
The response fields are not annotated
 */
@ResponseType(type = ResponseTypes.JSON)
@RestCommand(restcommand = "shooting/liveview",restverb = RestVerbs.POST)
public class LiveViewToggle implements CCAPIPojos {

    
    private String message;

    @JsonProperty
    @PossiblePostValuesForUI(possiblevalues = {"small","medium"})
    @PossiblePostValues(possiblevalues = {"off","small","medium"})
    private String liveviewsize;

    @JsonProperty
    @PossiblePostValuesForUI(possiblevalues = {"off","on"})
    @PossiblePostValues(possiblevalues = {"off","on"})
    private String cameradisplay;

    public LiveViewToggle(){}

    //this is the post body constructor
    public LiveViewToggle(String liveviewsize, String cameradisplay) {
        this.liveviewsize = liveviewsize;
        this.cameradisplay = cameradisplay;
    }

    public LiveViewToggle(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLiveviewsize() {
        return liveviewsize;
    }

    public void setLiveviewsize(String liveviewsize) {
        this.liveviewsize = liveviewsize;
    }

    public String getCameradisplay() {
        return cameradisplay;
    }

    public void setCameradisplay(String cameradisplay) {
        this.cameradisplay = cameradisplay;
    }

    @Override
    public String toString() {
        return "LiveViewOn{" +
                "message='" + message + '\'' +
                '}';
    }
}
