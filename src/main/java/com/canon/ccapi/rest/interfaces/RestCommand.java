package com.canon.ccapi.rest.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RestCommand {
    String restcommand();
    RestVerbs restverb() default RestVerbs.GET;
}