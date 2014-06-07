package org.arungupta.raspi.camera.rest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author arungupta
 */
@Path("images")
public class ImageConsumer {

    final DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    java.nio.file.Path tempDirectory;

    static final Logger LOGGER = Logger.getLogger(ImageConsumer.class.getName());

    // Make sure this directory exists! It will not be created automatically.
    private static final java.nio.file.Path BASE_DIR = Paths.get(System.getProperty("user.home"), "raspi");
    
    @POST
    @Consumes(MediaType.WILDCARD)
    public void receiveFile(InputStream is) {
        String fileName = df.format(Calendar.getInstance().getTime()) + ".png";

        LOGGER.log(Level.INFO, "Received REST request: receiveFile");
        try {
            Files.copy(is, BASE_DIR.resolve(fileName));
            LOGGER.log(Level.INFO, "File copied to path: {0}", BASE_DIR.resolve(fileName).toString());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    /*
     * Returns a list of available image names.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray getFileNames() throws IOException {
        
        // Filter out all non-PNG files.
        DirectoryStream.Filter<java.nio.file.Path> filter = new DirectoryStream.Filter<java.nio.file.Path>() {
            @Override
            public boolean accept(java.nio.file.Path entry) throws IOException {
                return entry.getFileName().toString().endsWith("png");
            }
        };
        
        // Browse the filtered BASE_DIR and list all the files.
        JsonArrayBuilder results = Json.createArrayBuilder();
        for (java.nio.file.Path entry : Files.newDirectoryStream(BASE_DIR, filter)) {
            results.add(entry.getFileName().toString());
        }
        return results.build();
    }
    
    /*
     * Download an image file.
     */
    @GET
    @Path("{name}.png")
    @Produces("image/png")
    public InputStream getImage(@PathParam("name") String fileName) throws IOException {
        
        fileName += ".png";
        java.nio.file.Path dest = BASE_DIR.resolve(fileName);
        
        if (!Files.exists(dest)) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        
        return Files.newInputStream(dest);
    }
}
