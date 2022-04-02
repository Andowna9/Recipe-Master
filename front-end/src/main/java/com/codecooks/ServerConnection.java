package com.codecooks;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.oauth2.OAuth2ClientSupport;


public class ServerConnection {

    private static ServerConnection instance = null;

    private final WebTarget webTarget;
    private static final String BASE_URI = "http://localhost:8080/";

    private ServerConnection() {
        Client client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URI);
    }

    public static ServerConnection getInstance() {

        if(instance == null) {
            instance = new ServerConnection();
        }
        return instance;
    }

    public WebTarget getTarget(String path) {

        return webTarget.path(path);
    }

    public void setAuthToken(String token) {

        webTarget.register(OAuth2ClientSupport.feature(token));
    }

}
