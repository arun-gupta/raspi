package org.arungupta.raspi.camera.webapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;

/**
 * @author Arun Gupta
 */
@Dependent
public class Camera {

    private static final Logger LOGGER = Logger.getLogger(Camera.class.getName());

    private static final String[] IMAGE_EFFECT = {
        "none", // No effect
        "negative", // Produces a negative image 
        "solarise", // Solarise the image 
        "whiteboard", // Whiteboard effect 
        "blackboard", // Blackboard effect 
        "sketch", // Sketch-style effect 
        "denoise", // Denoise the image 
        "emboss", // Embossed effect 
        "oilpaint", // Oil paint-style effect 
        "hatch", // Cross-hatch sketch style 
        "gpen", // Graphite sketch style 
        "pastel", // Pastel effect 
        "watercolour", // Watercolour effect 
        "film", // Grainy film effect 
        "blur", // Blur the image 
        "saturation", // Colour-saturate the image 
    };

    Random RANDOM = new Random();
    
    public void takePicture(String fileName, String directoryName) {
        try {
            Runtime run = Runtime.getRuntime();
            if (null == directoryName) {
                directoryName = ".";
            }

//            String cmd = "raspistill -o " + directoryName + File.separator + fileName + ".jpg -rot 180";
            String cmd = "raspistill " 
                    + "-o " + directoryName + File.separator + fileName + ".jpg "  // file name
                    + "-n " // Do not display a preview window
                    + "-q 50 " // JPEG quality
                    + "-ifx " + IMAGE_EFFECT[RANDOM.nextInt(16)] // Image effect
                    ;
            Process p = run.exec(cmd);
            Logger.getAnonymousLogger().log(Level.INFO, "Camera Command: {0}", cmd);
            p.waitFor();
            logProcessOutput(p);
        } catch (IOException | InterruptedException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void takePicture2(String fileName, String directoryName) {
        try {
            Files.copy(Paths.get("/Users/arungupta/Pictures/wildfly.jpg"), Paths.get(directoryName, fileName + ".jpg"));
        } catch (IOException ex) {
            Logger.getLogger(Camera.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void logProcessOutput(Process p) throws IOException {
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

        String s;

        // read the output from the command
        System.out.println("Here is the standard output of the command:\n");
        while ((s = stdInput.readLine()) != null) {
            LOGGER.log(Level.INFO, s);
        }

        // read any errors from the attempted command
        System.out.println("Here is the standard error of the command (if any):\n");
        while ((s = stdError.readLine()) != null) {
            LOGGER.log(Level.WARNING, s);
        }
    }

}
