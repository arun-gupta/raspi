package org.arungupta.raspi.camera.webapp;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author arungupta
 */
@Singleton
public class RestClient {
    
    static final Logger LOGGER = Logger.getLogger(RestClient.class.getName());
//    private final static String endpoint = "http://localhost:8080/camera-rest/webresources/images";
    private final static String ENDPOINT = "http://raspi-milestogo.rhcloud.com/webresources/images";

    public void sendFile(InputStream is) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(ENDPOINT);
        Response response = target.request().post(Entity.entity(is, MediaType.APPLICATION_OCTET_STREAM));
        LOGGER.log(Level.INFO, "sent InputStream to REST, received: {0}", response.getStatus());
        client.close();
    }
}
