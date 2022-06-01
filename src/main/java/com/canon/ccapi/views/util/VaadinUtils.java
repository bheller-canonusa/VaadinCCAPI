package com.canon.ccapi.views.util;

import com.canon.ccapi.rest.interfaces.CCAPIPojos;
import com.canon.ccapi.rest.interfaces.PossiblePostValuesForUI;
import com.canon.ccapi.rest.interfaces.RestCommand;
import com.canon.ccapi.rest.model.ErrorMessage;
import com.canon.ccapi.rest.returnobjects.RegularCCAPIReturnObject;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import org.springframework.core.annotation.AnnotationUtils;

import javax.management.Notification;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class VaadinUtils {



    public static void createRadioGroup(RadioButtonGroup rbg,CCAPIPojos ccapipojo,String fieldname){
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

        rbg.setItems(s);
    }


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
