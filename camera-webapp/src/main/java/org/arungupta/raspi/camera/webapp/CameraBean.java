package org.arungupta.raspi.camera.webapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * @author arungupta
 */
@Stateless
public class CameraBean {
    
    static final Logger LOGGER = Logger.getLogger(CameraBean.class.getName());
    @Inject Camera camera;
    @Inject RestClient restClient;
    final DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    @Schedule(hour = "*", minute = "*", second = "*/30")
    public void takePicture() {
        try {
            String fileName = df.format(Calendar.getInstance().getTime());
            camera.takePicture(fileName, ".");
            Path path = Paths.get(".", fileName + ".jpg");
            LOGGER.log(Level.INFO, "path: {0}", path.toString());
            restClient.sendFile(Files.newInputStream(path));
        } catch (IOException ex) {
            Logger.getLogger(CameraBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
