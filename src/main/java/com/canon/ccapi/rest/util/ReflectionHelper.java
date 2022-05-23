package com.canon.ccapi.rest.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class ReflectionHelper {

    public static Field getFieldAtAnnotation(Object o, Class c){
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field:fields){
            if (field.isAnnotationPresent(c)){
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    public static List<String> getAnnotationValues(Class base, Class annotation){
        Annotation[] an = base.getAnnotations();
        return null;


    }



}
