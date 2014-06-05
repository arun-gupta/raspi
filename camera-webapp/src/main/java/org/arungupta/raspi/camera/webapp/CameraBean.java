package org.arungupta.raspi.camera.webapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * @author arungupta
 */
@Stateless
public class CameraBean {
    
    @Inject Camera camera;
    final DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    @Schedule(hour = "*", minute = "*", second = "*/30")
    public void takePicture() {
        String fileName = df.format(Calendar.getInstance().getTime());
        camera.takePicture(fileName, ".");
    }
}
