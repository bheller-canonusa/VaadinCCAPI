package com.canon.ccapi.rest.exceptions;

public class InconsistantStateException extends RuntimeException{

    public InconsistantStateException() {
    }

    public InconsistantStateException(String message) {
        super(message);
    }

    public InconsistantStateException(Throwable cause) {
        super(cause);
    }
}
