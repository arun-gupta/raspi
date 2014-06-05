package org.arungupta.raspi.camera.rest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author arungupta
 */
public class ImageConsumerTest {
    
    WebTarget target;
    private final static String endpoint = "http://localhost:8080/camera-rest/webresources/images";
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        Client client = ClientBuilder.newClient();
        target = client.target(endpoint);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of receiveFile method, of class ImageConsumer.
     */
//    @Test
    public void testReceiveFile() {
        try (InputStream is = Files.newInputStream(FileSystems.getDefault().getPath(System.getProperty("user.home"), "Desktop", "wildfly-logo.png"))) {
            target.request().post(Entity.entity(is, MediaType.APPLICATION_OCTET_STREAM));
        } catch (IOException ex) {
            Logger.getLogger(ImageConsumerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
