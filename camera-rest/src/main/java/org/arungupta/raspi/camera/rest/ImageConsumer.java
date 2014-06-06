package org.arungupta.raspi.camera.rest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author arungupta
 */
@Path("images")
public class ImageConsumer {

    final DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    java.nio.file.Path tempDirectory;

//    @Context
//    HttpServletRequest request;

    static final Logger LOGGER = Logger.getLogger(ImageConsumer.class.getName());

//    @PostConstruct
//    public void init() {
//        try {
//            tempDirectory = Files.createTempDirectory("raspi");
//        } catch (IOException ex) {
//            LOGGER.log(Level.SEVERE, null, ex);
//        }
//    }

    @POST
    @Consumes(MediaType.WILDCARD)
    public void receiveFile(InputStream is) {
        String fileName = df.format(Calendar.getInstance().getTime()) + ".png";

        LOGGER.log(Level.INFO, "Received REST request: receiveFile");
        try {
//            java.nio.file.Path path = Paths.get(tempDirectory.toString(), fileName);
            java.nio.file.Path path = Paths.get(System.getProperty("user.home"), fileName);
            Files.copy(is, path);
            LOGGER.log(Level.INFO, "File copied to path: {0}", path.toString());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}
