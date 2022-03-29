package com.codecooks;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.JerseyWebTarget;

public class ServerConnection {

    WebTarget webTarget;
    ServerConnection instance = null;

    public ServerConnection getInstance() {
        if(instance == null) {
            instance = new ServerConnection();
        }
        return instance;
    }

    private ServerConnection() {
        Client client = ClientBuilder.newClient();
        webTarget = client.target("http://localhost:8080/");

    }

    public WebTarget getTarget(){

        return webTarget;
    }
}
