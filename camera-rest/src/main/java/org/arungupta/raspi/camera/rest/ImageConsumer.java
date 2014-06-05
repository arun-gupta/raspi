package org.arungupta.raspi.camera.rest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author arungupta
 */
@Path("images")
public class ImageConsumer {

//    @POST
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    public void receiveFile(@FormParam("file") InputStream is, @FormParam("name")String name) {
//        try {
//            java.nio.file.Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "images", name);
//            Files.copy(is, path);
//        } catch (IOException ex) {
//            Logger.getLogger(ImageConsumer.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    @POST
//    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.WILDCARD)
    public void receiveFile(InputStream is) {
        Logger.getAnonymousLogger().log(Level.INFO, "Received REST request: receiveFile");
        try {
            java.nio.file.Path path = FileSystems.getDefault().getPath(System.getProperty("user.home"), "Documents");
            Files.copy(is, path);
        } catch (IOException ex) {
            Logger.getLogger(ImageConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
