package org.arungupta.raspi.camera.rest;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
    private static final java.nio.file.Path THUMB_DIR = Paths.get(System.getProperty("user.home"), "raspi", "thumbs");
    
    @POST
    @Consumes(MediaType.WILDCARD)
    public void receiveFile(InputStream is) throws IOException {
        String fileName = df.format(Calendar.getInstance().getTime()) + ".jpg";

        LOGGER.log(Level.INFO, "Received REST request: receiveFile");
        try {
            Files.copy(is, BASE_DIR.resolve(fileName));
            LOGGER.log(Level.INFO, "File copied to path: {0}", BASE_DIR.resolve(fileName).toString());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        
        // Generate a 150x150 thumbnail.
        BufferedImage image = ImageIO.read(Files.newInputStream(BASE_DIR.resolve(fileName)));
        if (null == image) {
            LOGGER.log(Level.SEVERE, "{0} could not be read as BufferedImage, getting null", BASE_DIR.resolve(fileName).toString());
            Files.copy(BASE_DIR.resolve(fileName), THUMB_DIR.resolve(fileName));
            return;
        }
        BufferedImage scaledImage = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, 150, 150, 0, 0, image.getWidth(), image.getHeight(), null);
        g.dispose();
        OutputStream out = Files.newOutputStream(THUMB_DIR.resolve(fileName), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        ImageIO.write(scaledImage, "jpg", out);
    }
    
    /*
     * Returns a list of available image names.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray getFileNames() throws IOException {
        
        // Filter out all non-JPG files.
        DirectoryStream.Filter<java.nio.file.Path> filter = new DirectoryStream.Filter<java.nio.file.Path>() {
            @Override
            public boolean accept(java.nio.file.Path entry) throws IOException {
                return entry.getFileName().toString().endsWith("jpg");
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
    @Path("{name}.jpg")
    @Produces("image/jpg")
    public InputStream getImage(@PathParam("name") String fileName) throws IOException {
        
        fileName += ".jpg";
        java.nio.file.Path dest = BASE_DIR.resolve(fileName);
        
        if (!Files.exists(dest)) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        
        return Files.newInputStream(dest);
    }
    
    /*
     * Download a thumbnail.
     */
    @GET
    @Path("thumbs/{name}.jpg")
    @Produces("image/jpg")
    public InputStream getThumbnail(@PathParam("name") String fileName) throws IOException {
        
        fileName += ".jpg";
        java.nio.file.Path dest = THUMB_DIR.resolve(fileName);
        
        if (!Files.exists(dest)) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        
        return Files.newInputStream(dest);
    }
}
