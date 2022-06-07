package com.canon.ccapi;

import com.canon.ccapi.rest.consumer.RestConsumer;
import com.canon.ccapi.rest.exceptions.Non200ReturnException;
import com.canon.ccapi.rest.interfaces.CCAPIPojos;
import com.canon.ccapi.rest.interfaces.RestCommand;
import com.canon.ccapi.rest.model.ErrorMessage;
import com.canon.ccapi.rest.model.liveview.LiveViewImage;
import com.canon.ccapi.rest.model.liveview.LiveViewToggle;
import com.canon.ccapi.rest.model.status.BatteryStatus;
import com.canon.ccapi.rest.model.storage.StorageName;
import com.canon.ccapi.rest.util.ReflectionHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@SpringBootTest
public class CCAPIRestClientApplicationTests {

    @Autowired
    private RestConsumer restconsume;

    @Test
    void contextLoads() throws Exception {

       // LiveViewToggle tt = new LiveViewToggle();

       // ReflectionHelper.getAnnotationValues(tt)



        /*
        System.setProperty("java.awt.headless", "false");
        File file = new File("C:\\Users\\c16144\\IdeaProjects\\VaadinCCAPI\\src\\main\\resources\\META-INF\\resources\\images\\jpegtest.jpg");

      //  ImagePlus imp = IJ.openImage("C:\\Users\\c16144\\IdeaProjects\\VaadinCCAPI\\src\\main\\resources\\META-INF\\resources\\images\\jpegtest.jpg");
     // imp.show();

        JFrame jFrame = new JFrame();
        jFrame.setLayout(new FlowLayout());

        jFrame.setSize(500, 500);
        JLabel jLabel = new JLabel();
        jFrame.add(jLabel);
        jFrame.setVisible(true);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        for (int i = 0;i<1000;i++) {
            List lt = restconsume.makeCall(new LiveViewImage());
            LiveViewImage lvi = (LiveViewImage) lt.get(0);


            byte[] bb = lvi.getImage();
            InputStream is = new ByteArrayInputStream(bb);

            BufferedImage buffimage = ImageIO.read(is);
            ImageIcon imageIcon = new ImageIcon(buffimage);


            jLabel.setIcon(imageIcon);




            Thread.sleep(500);

        }
        //try {

            System.out.println(restconsume.makeCall(new LiveViewToggle("small","on")));
            //System.out.println(restconsume.makeCall(new LiveViewToggle("small","off")));
      //      restconsume.makeCall(new LiveViewImage());


      //  }
     //   catch(Exception e){
     //       e.printStackTrace();
     //   }
*/
/*
        LiveViewImage test = new LiveViewImage();
        //BatteryStatus b = new BatteryStatus();
        //BatteryStatus bb  = restconsume.makeCall(b).getRegular();
        try {
            LiveViewImage out = restconsume.makeCall(test).getRegular();

            System.out.println(out.getMessage());
        }
        catch(Non200ReturnException e){
            ErrorMessage ee = e.getErrorMessage();
            System.out.println("Error message is:"+ee);
        }
*/
      //  System.out.println(restconsume.makeCall(new LiveViewToggle("off","off")));

        StorageName sn = restconsume.makeCall(new StorageName(),"").getRegular();
        String restcommand = sn.getClass().getAnnotation(RestCommand.class).restcommand();

        System.out.println(restcommand);

        //assuming only one storage url
        String[] ss = sn.getUrl();
        String storageurl = ss[0];









        for (String s:ss){
            System.out.println(s);
        }


    }

}
