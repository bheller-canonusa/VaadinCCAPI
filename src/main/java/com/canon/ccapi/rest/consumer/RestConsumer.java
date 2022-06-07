package com.canon.ccapi.rest.consumer;

import com.canon.ccapi.rest.constants.Constants;
import com.canon.ccapi.rest.exceptions.Non200ReturnException;
import com.canon.ccapi.rest.interfaces.*;

import com.canon.ccapi.rest.model.ErrorMessage;
import com.canon.ccapi.rest.returnobjects.RegularCCAPIReturnObject;
import com.canon.ccapi.rest.util.ReflectionHelper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


import java.io.IOException;
import java.lang.reflect.Field;




@Scope("singleton")
@Service
public class RestConsumer {

    @Value("${restconsumer.debug}")
    private Boolean debug;

    private final WebClient webClient2;

    private static final Logger logger = LoggerFactory.getLogger(RestConsumer.class);


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


    public RegularCCAPIReturnObject makeCall(CCAPIPojos makecallobject){
        return makeCall(makecallobject,null);
    }


    public RegularCCAPIReturnObject makeCall(CCAPIPojos makecallobject,String modifyurl) {
        if (debug) {
            logger.info("Rest call using POJO-->" + makecallobject.getClass() + "<---->"+ makecallobject + "<--");
        }

        Class<? extends CCAPIPojos> clazz = makecallobject.getClass();
        RestVerbs verb = clazz.getAnnotation(RestCommand.class).restverb();
        String command = clazz.getAnnotation(RestCommand.class).restcommand();
        if (modifyurl!=null){
            command=command.replace("{?}",modifyurl);
        }

        ResponseTypes type = clazz.getAnnotation(ResponseType.class).type();

        String postbodyvalues = null;

        //need to make a post body and object should have these values set
        if (verb == RestVerbs.POST){
         postbodyvalues = createBodyJson(makecallobject);
        }


        try {

            switch (verb) {
                case GET:

                    if (type == ResponseTypes.IMAGEJPEG) {
                        byte[] image = null;


                        image = webClient2.get().uri(command).accept(MediaType.APPLICATION_JSON).retrieve()
                                .toEntity(byte[].class).block().getBody();


                        Object o = null;
                        try {
                            o = clazz.getConstructor().newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Field f = ReflectionHelper.getFieldAtAnnotation(o, ImageTag.class);
                        if (f != null) {
                            try {
                                f.set(o, image);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            throw new RuntimeException("No Image Annotation Present");
                        }


                        return new RegularCCAPIReturnObject(o);


                    } else {

                        return new RegularCCAPIReturnObject(webClient2.get().uri(command).accept(MediaType.APPLICATION_JSON).retrieve()
                                .toEntity(clazz).block().getBody());

                    }

                case POST:

                    return new RegularCCAPIReturnObject(webClient2.post().uri(command).contentType(MediaType.APPLICATION_JSON).bodyValue(postbodyvalues).retrieve()
                            .toEntity(clazz).block().getBody());

                default:
                    throw new RuntimeException("unknown verb");
            }

        }
        catch(WebClientResponseException e){
            ErrorMessage errormessage= null;
            try {
                errormessage= mapper.readValue(e.getResponseBodyAsString(), ErrorMessage.class);
            }
            catch (JsonProcessingException ee){
               logger.error("jsonprocessing->",ee);
            }
            if (debug) {
                logger.info("Error raw-->" + e + "<--");
            }
            throw new Non200ReturnException(errormessage);
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
