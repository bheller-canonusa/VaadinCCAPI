package com.canon.ccapi.views;

import com.canon.ccapi.rest.consumer.RestConsumer;
import com.canon.ccapi.rest.model.status.BatteryStatus;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value="camera-status",layout= MainView.class)
@PageTitle("Camera Status")
public class CameraStatusView extends HorizontalLayout {



    public CameraStatusView(@Autowired RestConsumer service){
            final Grid<BatteryStatus> camerastatus = new Grid<>();

            camerastatus.addColumn(batterystatus->batterystatus.getKind()).setHeader("Kind of Battery").setTextAlign(ColumnTextAlign.END);
            camerastatus.addColumn(batteryStatus -> batteryStatus.getLevel()).setHeader("Battery Level");
            camerastatus.addColumn(batteryStatus -> batteryStatus.getName()).setHeader("Battery Name");
            camerastatus.addColumn(batteryStatus -> batteryStatus.getQuality()).setHeader("Battery Quality");

            //camerastatus.setItems(service.getStorageAsJson());

            add(camerastatus);

    }


}
