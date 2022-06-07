package com.canon.ccapi.views;

import com.canon.ccapi.rest.consumer.RestConsumer;
import com.canon.ccapi.rest.exceptions.Non200ReturnException;
import com.canon.ccapi.rest.model.ErrorMessage;
import com.canon.ccapi.rest.model.liveview.LiveViewImage;
import com.canon.ccapi.rest.model.liveview.LiveViewToggle;
import com.canon.ccapi.rest.model.shootingcontrol.StillImageCapture;
import com.canon.ccapi.views.util.VaadinUtils;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.communication.PushMode;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;
import org.springframework.beans.factory.annotation.Autowired;



import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


@Route(value="live-view",layout=MainView.class)
@PageTitle("Live View")
public class LiveView extends VerticalLayout {

    @Autowired
    private RestConsumer myrestconsumer;
    private Button liveviewbutton,captureimage;
    private ImageReloader irl;
    private ComboBox<String> sizeofimage,displayonoff;

    private RadioButtonGroup<String> radiogroupaf;

    private VerticalLayout vcapturelayout;

    private Boolean liveviewstart;

    private AtomicLong picturetaken;

    private Image liveviewimage;

    private AtomicBoolean shouldnotify;

    public LiveView() {

        setId("liveview-view");

        liveviewstart = false;

        liveviewimage = new Image();

        shouldnotify = new AtomicBoolean(false);

        picturetaken = new AtomicLong(0);

        createButtons();
        createDropDowns();
        createRadioButton();

        vcapturelayout = new VerticalLayout();
        radiogroupaf.setWidth("100%");
        captureimage.setWidth("100%");
        vcapturelayout.add(radiogroupaf,captureimage);
        vcapturelayout.setPadding(false);
        vcapturelayout.setSpacing(false);
        vcapturelayout.setHorizontalComponentAlignment(Alignment.END);



        HorizontalLayout hl = new HorizontalLayout();
        hl.setWidth("100%");

        liveviewbutton.setWidth("50%");
        vcapturelayout.setWidth("50%");
        sizeofimage.setWidth("50%");
        displayonoff.setWidth("50%");


        hl.add(liveviewbutton,vcapturelayout,sizeofimage,displayonoff);
        hl.setSpacing(true);
        hl.setVerticalComponentAlignment(Alignment.END,liveviewbutton,vcapturelayout,sizeofimage,displayonoff);
        add(hl);

        add(liveviewimage);

    }

    public AtomicLong getPictureTime(){return picturetaken;}

    public AtomicBoolean getShouldnotify(){
        return shouldnotify;
    }

    private boolean getAFValue(){
        if (radiogroupaf.getValue().equals(null)){
            return true;
        }
        if(radiogroupaf.getValue().equals("true")) {
            return true;
        }
        else{
            return false;
        }
    }

    private void createRadioButton(){
        radiogroupaf = new RadioButtonGroup<>();
        VaadinUtils.createRadioGroup(radiogroupaf,new StillImageCapture(),"af");
        radiogroupaf.setLabel("AF");
        radiogroupaf.setValue("true");
        radiogroupaf.addValueChangeListener(e->{

        });
    }

    public Boolean getLiveViewState(){return liveviewstart;}
    public void setLiveViewState(Boolean b){liveviewstart = b;}

    private void createDropDowns(){
        sizeofimage = new ComboBox<>("Size of Image");
        displayonoff = new ComboBox<>("Display on/off");



        VaadinUtils.createDropDown(sizeofimage,new LiveViewToggle(),"liveviewsize");
        VaadinUtils.createDropDown(displayonoff,new LiveViewToggle(),"cameradisplay");

        sizeofimage.addValueChangeListener(e->{

                if (liveviewstart) {
                    myrestconsumer.makeCall(new LiveViewToggle(e.getValue(), (displayonoff.getValue()==null)?"on":displayonoff.getValue()));
                }
        });

        displayonoff.addValueChangeListener(e->{

                if (liveviewstart){
                    myrestconsumer.makeCall(new LiveViewToggle((sizeofimage.getValue()==null)?"small":sizeofimage.getValue(), e.getValue()));
                }
                else{
                    myrestconsumer.makeCall(new LiveViewToggle("off", e.getValue()));
                }

        });

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
    }

    private void createButtons() {

        liveviewbutton = new Button("Start Live View");

        liveviewbutton.addClickListener(e -> {
            if (liveviewbutton.getText().equalsIgnoreCase("start live view")) {

                liveviewbutton.setText("Stop Live View");
                try {
                    myrestconsumer.makeCall(new LiveViewToggle((sizeofimage.getValue()==null)?"small":sizeofimage.getValue(),"on"));
                } catch (Exception ee) {
                    Notification.show("General Error, See Logs ");
                    ee.printStackTrace();
                }

                irl = new ImageReloader(UI.getCurrent(),this,myrestconsumer);
                irl.start();

                liveviewstart = true;

            } else if (liveviewbutton.getText().equalsIgnoreCase("stop live view")) {

                liveviewbutton.setText("Start Live View");

                try {
                    myrestconsumer.makeCall(new LiveViewToggle("off", (displayonoff.getValue()==null)?"off":displayonoff.getValue()));
                } catch (Exception ee) {
                    Notification.show("General Error, See Logs");
                    ee.printStackTrace();
                }
                irl.endLiveView();
                liveviewstart = false;
            }

        });

        captureimage = new Button("Capture Image");

        captureimage.addClickListener(c->{

            try{

            if(getLiveViewState()) {

                getPictureTime().set(System.currentTimeMillis());
                //getShouldnotify().set(true);


            }

                myrestconsumer.makeCall(new StillImageCapture(getAFValue()));
            }
            catch (Non200ReturnException ee){
                ErrorMessage em = ee.getErrorMessage();
                Notification.show("ERROR: "+em.getMessage()+"\n CODE: "+em.getErrorcode());
            }

        //    if (getLiveViewState()){

               //     getShouldnotify().set(false);
                  //  synchronized (irl) {
                  //     irl.notify();
                  //  }
         //   }
        });
    }

    private static class ImageReloader extends Thread {
        private final UI ui;
        private final LiveView view;

        private AtomicBoolean flag;

        private RestConsumer myrestconsumer;

        public ImageReloader(UI ui, LiveView imr, RestConsumer r) {
            this.ui = ui;
            this.view = imr;
            this.myrestconsumer = r;
            flag = new AtomicBoolean(true);
        }



        public void startLiveView() {
            flag.set(true);
        }

        public void endLiveView() {
            flag.set(false);
        }


        @Override
        public void run() {
            System.out.println("Live View Thread Running Name-->" + Thread.currentThread().getName() + "<--");

            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }


            while (flag.get()) {

                try {
                    LiveViewImage im = myrestconsumer.makeCall(new LiveViewImage()).getRegular();
                    Thread.sleep(250);
                    byte[] b = im.getMyimage();
                    StreamResource streamresource = new StreamResource("isr", new InputStreamFactory() {
                        @Override
                        public InputStream createInputStream() {
                            return new ByteArrayInputStream(b);
                        }
                    });

                    ui.access(() -> {
                        view.liveviewimage.setSrc(streamresource);
                        //ui.push();
                    });

                    if (view.getShouldnotify().get()) {
                        System.out.println("Picture taken, Sleeping");
                            Thread.currentThread().sleep(2000);

                        System.out.println("Picture taken, waking, should I sleep more?");

                    }


                   // if (view.getShouldnotify().get()) {
                     // synchronized (this){
                       //   System.out.println(Thread.currentThread().getName()+" Before Wait");
                       //   wait();
                       //   System.out.println(Thread.currentThread().getName()+" After Wait");

                      //    try{
                      //        Thread.currentThread().sleep(500);
                      //    }
                      //    catch (InterruptedException e){
                     //         e.printStackTrace();
                      //    }
                     //     waitUntilCameraNotBusy();

                          //try{
                          //    Thread.currentThread().sleep(4000);
                         // }
                         // catch (InterruptedException e){
                         //     e.printStackTrace();
                        //  }
                     // }
                  //  }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catch(Non200ReturnException e){
                    //need a guard against error messages showing up after an image is captured because
                    //camera is not in ready state to keep live view images showing...usually device busy messages

                    long delta = System.currentTimeMillis() - view.getPictureTime().get();
                    System.out.println("delta (outside)->"+delta);
                    if (delta > 10000l) {
                        System.out.println("delta (inside)->"+delta);
                        ErrorMessage em = e.getErrorMessage();
                        ui.access(() -> {
                            Notification.show("ERROR: " + em.getMessage() + "\n CODE: " + em.getErrorcode());
                            view.liveviewbutton.setText("Start Live View");
                            view.setLiveViewState(false);
                        });
                        endLiveView();
                    }

                }

            }
            System.out.println("Live View Thread Ending Name-->" + Thread.currentThread().getName() + "<--");
        }


        private void waitUntilCameraNotBusy()  {

            System.out.println("Entering waituntilcameranotbusy");
            try {
                while (true) {
                    Thread.sleep(1000);
                    try {
                        LiveViewImage im = myrestconsumer.makeCall(new LiveViewImage()).getRegular();
                        return;
                    }
                    catch(Non200ReturnException e){
                        System.out.println("Not Yet Ready");
                    }

                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }

            System.out.println("Leaving waituntilcameranotbusy");
        }


    }
}