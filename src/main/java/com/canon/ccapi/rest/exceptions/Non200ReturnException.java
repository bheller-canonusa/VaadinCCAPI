package com.canon.ccapi.rest.exceptions;


import com.canon.ccapi.rest.model.ErrorMessage;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public class Non200ReturnException extends RuntimeException {


    private ErrorMessage error;
    private String errorcode;

    public Non200ReturnException(){
        System.out.println("dfg");
    }

    public Non200ReturnException(ErrorMessage e){
        this.error=e;errorcode=null;
    }

    public Non200ReturnException(ErrorMessage e,String ec){
        this.error=e;errorcode=ec;
    }

    public String getErrorcode(){return errorcode;}

    public ErrorMessage getErrorMessage(){return error;}

}
