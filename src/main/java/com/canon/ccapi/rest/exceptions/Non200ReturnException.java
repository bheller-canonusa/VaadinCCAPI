package com.canon.ccapi.rest.exceptions;


import com.canon.ccapi.rest.model.ErrorMessage;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public class Non200ReturnException extends RuntimeException {

    private String message;
    private Mono body;

    public Non200ReturnException(){
        System.out.println("dfg");
    }

    public Non200ReturnException(String s){
        this.message=s;
    }

    public String getMessage(){return message;}

    public Mono getMonoBody(){
        return body;
    }

    public Non200ReturnException(Mono m){
        this.body=m;

        //Object mm = m.block();

        //m.subscribe(v->System.out.println(v),e->System.out.println(e),()->System.out.println("completed without value"));

       //m.log().subscribe(System.out::println);



      // System.out.println("sdf-->"+mm);

    }


}
