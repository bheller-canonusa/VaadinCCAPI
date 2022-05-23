package com.canon.ccapi.rest.consumer;

import com.canon.ccapi.rest.constants.Constants;
import com.canon.ccapi.rest.interfaces.*;

import com.canon.ccapi.rest.util.ReflectionHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;


@Scope("singleton")
@Service
public class RestConsumer {

    private final WebClient webClient2;

    @Autowired
    private ObjectMapper mapper;


    public RestConsumer(){
        this.webClient2 = WebClient.builder()
                .baseUrl(Constants.BASE_URL+":"+Constants.BASE_PORT+"/ccapi/"+Constants.API_VER_100+"/")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


    /*

    MIGHT NEED THIS:

    webClient
    .post()
    .uri(uriBuilder ->
        uriBuilder.pathSegment("users", "{userId}")
            .build(userId))

     */

    private String createBodyJson(CCAPIPojos c){

    String json = "";

        try {
            json =  mapper.writeValueAsString(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return json;
    }

    public <T extends CCAPIPojos> T makeCall(CCAPIPojos makecallobject) {
        Class<? extends CCAPIPojos> clazz = makecallobject.getClass();
        RestVerbs verb = clazz.getAnnotation(RestCommand.class).restverb();
        String command = clazz.getAnnotation(RestCommand.class).restcommand();
        ResponseTypes type = clazz.getAnnotation(ResponseType.class).type();

        String postbodyvalues = null;

        //need to make a post body and object should have these values set
        if (verb == RestVerbs.POST){
         postbodyvalues = createBodyJson(makecallobject);
        }


        switch (verb){
            case GET:

                if (type == ResponseTypes.IMAGEJPEG){
                    byte[] image = webClient2.get().uri(command).accept(MediaType.IMAGE_JPEG).retrieve().toEntity(byte[].class).block().getBody();
                    Object o = null;
                    try {
                        o = clazz.getConstructor().newInstance();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    Field f = ReflectionHelper.getFieldAtAnnotation(o,ImageTag.class);
                    if (f!=null){
                        try{
                            f.set(o,image);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else{
                        throw new RuntimeException("No Image Annotation Present");
                    }


                    try {
                       //return Arrays.asList(Class.forName(clazz.getName()).cast(o));
                        return (T) Class.forName(clazz.getName()).cast(o);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                }
                else { //standard JSON body
                    return (T) webClient2.get().uri(command).accept(MediaType.APPLICATION_JSON).retrieve().toEntity(clazz).block().getBody();
                }

            case POST:
                //post may have {} as a body

                return (T) webClient2.post().uri(command).contentType(MediaType.APPLICATION_JSON).bodyValue(postbodyvalues).retrieve().toEntity(clazz).block().getBody();

            default:
                throw new RuntimeException("unknown verb");
        }
    }

/*
    public List<Battery> getStorageAsJson(){
       return webClient2.get()
                .uri("devicestatus/battery")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(Battery.class)
                .block()
                .getBody();
    }
*/



}
