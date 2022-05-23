package com.canon.ccapi.rest.model.liveview;

import com.canon.ccapi.rest.interfaces.*;
import com.fasterxml.jackson.annotation.JsonProperty;


@ResponseType(type = ResponseTypes.IMAGEJPEG)
@RestCommand(restcommand = "shooting/liveview/flip",restverb = RestVerbs.GET)
public class LiveViewImage implements CCAPIPojos {


    private String message;

    @ImageTag
    private byte[] myimage;


    public LiveViewImage(){}


    public LiveViewImage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getMyimage() {
        return myimage;
    }

    public void setMyimage(byte[] myimage) {
        this.myimage = myimage;
    }

    @Override
    public String toString() {
        return "LiveViewImage{" +
                "message='" + message + '\'' +
                '}';
    }
}
