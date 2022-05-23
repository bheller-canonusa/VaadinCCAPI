package com.canon.ccapi.views.util;

import com.canon.ccapi.rest.interfaces.CCAPIPojos;
import com.canon.ccapi.rest.interfaces.PossiblePostValuesForUI;
import com.canon.ccapi.rest.interfaces.RestCommand;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class VaadinUtils {


    public static void createDropDown(ComboBox c,CCAPIPojos ccapipojo,String fieldname){

        Class<? extends CCAPIPojos> clazz = ccapipojo.getClass();
        Field f;
        String[] s=null;

        try {
            f=clazz.getDeclaredField(fieldname);
            s=f.getAnnotation(PossiblePostValuesForUI.class).possiblevalues();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        c.setItems(s);

    }


}
