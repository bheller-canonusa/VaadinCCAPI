package com.canon.ccapi.rest.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseType {
    ResponseTypes type() default ResponseTypes.JSON;
}
