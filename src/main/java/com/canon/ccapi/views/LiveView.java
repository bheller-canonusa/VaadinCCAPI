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

    private Image liveviewimage;

    public LiveView() {

        setId("liveview-view");

        liveviewstart = false;

        liveviewimage = new Image();

        createButtons();
        createDropDowns();
        createRadioButton();

        vcapturelayout = new VerticalLayout();
        vcapturelayout.add(radiogroupaf,captureimage);
        vcapturelayout.setHorizontalComponentAlignment(Alignment.BASELINE);


        HorizontalLayout hl = new HorizontalLayout();
        hl.add(liveviewbutton,vcapturelayout,sizeofimage,displayonoff);
        hl.setSpacing(true);
        hl.setVerticalComponentAlignment(Alignment.END,liveviewbutton,vcapturelayout,sizeofimage,displayonoff);
        add(hl);

        add(liveviewimage);

    }


    private void createRadioButton(){
        radiogroupaf = new RadioButtonGroup<>();
        VaadinUtils.createRadioGroup(radiogroupaf,new StillImageCapture(),"af");
        radiogroupaf.setLabel("AF");


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

    private Boolean getCurrentLiveViewState() {

        LiveViewImage test = new LiveViewImage();

         LiveViewImage out = myrestconsumer.makeCall(test).getRegular();

        ImageReloader im = new ImageReloader(UI.getCurrent(), this, myrestconsumer);

        return false;
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
                    myrestconsumer.makeCall(new LiveViewToggle((sizeofimage.getValue()==null)?"small":displayonoff.getValue(),"on"));
                } catch (Exception ee) {
                    Notification.show("General Error, See Logs");
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
                myrestconsumer.makeCall(new StillImageCapture(true));
            }
            catch (Non200ReturnException ee){
                ErrorMessage em = ee.getErrorMessage();
                Notification.show("ERROR: "+em.getMessage());
            }

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
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                catch(Non200ReturnException e){
                    ErrorMessage em = e.getErrorMessage();
                    ui.access(()->{
                       Notification.show("ERROR: "+em.getMessage());
                       view.liveviewbutton.setText("Start Live View");
                       view.setLiveViewState(false);
                    });
                    endLiveView();
                }

            }
            System.out.println("Live View Thread Ending Name-->" + Thread.currentThread().getName() + "<--");
        }
    }
}





