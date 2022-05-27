package com.canon.ccapi.rest.model;

import com.canon.ccapi.rest.interfaces.CCAPIPojos;
import com.canon.ccapi.rest.interfaces.ResponseType;
import com.canon.ccapi.rest.interfaces.ResponseTypes;
import com.fasterxml.jackson.annotation.JsonProperty;

@ResponseType(type = ResponseTypes.JSON)
public class ErrorMessage implements CCAPIPojos {

    private String errorcode;

    @JsonProperty
    private String message;

    public ErrorMessage(){
        this.message = "{}";
    }

    public ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message,String errorcode) {
        this.message = message;this.errorcode=errorcode;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
