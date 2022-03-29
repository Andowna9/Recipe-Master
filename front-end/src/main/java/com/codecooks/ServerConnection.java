package com.codecooks;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;

public class ServerConnection {

    private WebTarget webTarget;
    private static ServerConnection instance = null;

    public static ServerConnection getInstance() {
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
