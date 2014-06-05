package org.arungupta.raspi.camera.webapp;

import java.io.InputStream;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;

/**
 * @author arungupta
 */
@Singleton
public class RestClient {
    
    WebTarget target;
    private final static String endpoint = "http://localhost:8080/camera-rest/webresources/images";

    @PostConstruct
    public void init() {
        Client client = ClientBuilder.newClient();
        target = client.target(endpoint);
    }
    
    public void sendFile(InputStream is, String name) {
        target.request().post(Entity.entity(is, MediaType.APPLICATION_OCTET_STREAM));
//        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
//        map.add("file", is);
//        map.add("name", name);
//        target.request().post(Entity.form(map));
    }
}
