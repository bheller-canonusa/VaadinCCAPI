package com.canon.ccapi.rest.returnobjects;

import com.canon.ccapi.rest.exceptions.InconsistantStateException;
import com.canon.ccapi.rest.interfaces.CCAPIPojos;
import com.canon.ccapi.rest.model.ErrorMessage;

public class RegularCCAPIReturnObject {

    private Object regular;
    private ErrorMessage error;


    public RegularCCAPIReturnObject(Object regular) {
        this.error=null;
        this.regular =  regular;
    }



    public RegularCCAPIReturnObject(ErrorMessage e){
        this.error=e;
        this.regular=null;
    }

    public Boolean isError(){
       return error!=null;
    }

    public Boolean isRegular(){
     return regular!=null;
    }




    public <T> T getRegular() {
        try {
            return (T) Class.forName(regular.getClass().getName()).cast(regular);
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }

    //we should only set regular if error is null
    public void setRegular(Object regular) {

        if (error==null) {
            this.regular = regular;
        }
        else{
            throw new InconsistantStateException("cannot set a regular object because error isn't null, error=>"+error);
        }

    }

    public ErrorMessage getError() {
        return error;
    }

    public void setError(ErrorMessage error) {

        if (regular==null){
            this.error=error;
        }
        else{
            throw new InconsistantStateException("cannot set an error object because regular isn't null, regular=>"+regular);
        }
    }

}
