package com.canon.ccapi;

import com.canon.ccapi.rest.consumer.RestConsumer;
import com.canon.ccapi.rest.interfaces.CCAPIPojos;
import com.canon.ccapi.rest.model.liveview.LiveViewImage;
import com.canon.ccapi.rest.model.liveview.LiveViewToggle;
import com.canon.ccapi.rest.model.status.BatteryStatus;
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

        LiveViewImage test = new LiveViewImage();
        LiveViewImage out = restconsume.makeCall(test);

        System.out.println(out.getMessage());
      //  System.out.println(restconsume.makeCall(new LiveViewToggle("off","off")));

    }

}
