package com.canon.ccapi.views;

import com.canon.ccapi.rest.consumer.RestConsumer;
import com.canon.ccapi.rest.exceptions.Non200ReturnException;
import com.canon.ccapi.rest.model.ErrorMessage;
import com.canon.ccapi.rest.model.status.BatteryStatus;
import com.canon.ccapi.rest.model.status.LensStatus;
import com.vaadin.flow.component.grid.CellFocusEvent;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Optional;


@Route(value="camera-status",layout= MainView.class)
@PageTitle("Camera Status")
public class CameraStatusView extends VerticalLayout {



    private RestConsumer myrestconsumer;

    private Grid<BatteryStatus> batterystatus;
    private Grid<LensStatus> lensstatus;
    private Label lens,battery;
    private TextArea ta;


    public CameraStatusView(@Autowired RestConsumer service){

        this.myrestconsumer = service;

        H1 lenstitle = new H1("Lens Information:");
        H1 batterytitle = new H1("Battery Information:");

        lenstitle.getStyle()
                .set("font-size", "var(--lumo-font-size-1)")
                .set("margin", "0");

        batterytitle.getStyle()
                .set("font-size", "var(--lumo-font-size-1)")
                .set("margin", "0");


        batterystatus = new Grid<>(BatteryStatus.class,true);
        batterystatus.setAllRowsVisible(true);
        batterystatus.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);

        lensstatus = new Grid<>(LensStatus.class,true);
        lensstatus.setAllRowsVisible(true);
        lensstatus.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);


        ta = new TextArea();
        ta.setLabel("Details:");
        ta.setReadOnly(true);



        add(batterytitle,batterystatus,lenstitle,lensstatus,ta);

        BatteryStatus bs=null;LensStatus ls=null;

        try {
             bs = myrestconsumer.makeCall(new BatteryStatus()).getRegular();
             ls = myrestconsumer.makeCall(new LensStatus()).getRegular();
        }
        catch (Non200ReturnException ee){
            ErrorMessage em = ee.getErrorMessage();
            Notification.show("ERROR: "+em.getMessage()+"\n CODE: "+em.getErrorcode());
        }

        batterystatus.setItems(bs);
        lensstatus.setItems(ls);

        batterystatus.addCellFocusListener(e->{
            createcellaction(e);
        });

        lensstatus.addCellFocusListener(e->{
            createcellaction(e);
        });


    }

    private void createcellaction(CellFocusEvent<?> e){
        CellFocusEvent.GridSection section = e.getSection();

        String col = e.getColumn().map(r->r.getKey()).orElse("N/A");

        String methname = "get"+col.substring(0,1).toUpperCase()+col.substring(1);
        String val = "";
        try{
            if (e.getItem().isPresent()) {
                Method m = e.getItem().get().getClass().getMethod(methname);
                val = m.invoke(e.getItem().get()) + "";
            }
        }
        catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException ee){
            ee.printStackTrace();
        }

        String row = e.getItem().toString();
        ta.setValue(col+"="+val);

    }


}
