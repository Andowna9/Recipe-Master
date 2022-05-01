package com.codecooks;

import com.codecooks.serialize.Credentials;
import com.codecooks.serialize.RegistrationData;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.client.Entity;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Integration Test for Account Resource API endpoint.
 */
public class AccountResourceIntegrationTest {

    private static HttpServer server;
    private static WebTarget target;
    private static String token;

    @BeforeClass
    public static void setUp() {

        server = Main.startServer();
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    // Register
    @Test
    public void testA() {

        WebTarget registerTarget = target.path("account");

        RegistrationData data = new RegistrationData();
        data.setUsername("Test");
        data.setEmail("test@gmail.com");
        data.setPassword("test");

        Response response = registerTarget.request()
                            .post(Entity.entity(data, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response.close();

    }

    // Login
    @Test
    public void testB() {

        WebTarget loginTarget = target.path("account/login");

        Credentials credentials = new Credentials();
        credentials.setEmail("test@gmail.com");
        credentials.setPassword("test");

        Response response = loginTarget.request(MediaType.TEXT_PLAIN)
                            .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        token = response.readEntity(String.class);
        response.close();

        assertNotNull(token);
    }

    // Logout
    @Test
    public void testC() {

        WebTarget logoutTarget = target.path("account/login");

        Response response = logoutTarget.request()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .delete();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response.close();
    }

    // Remove
    @Test
    public void testD() {

        WebTarget loginTarget = target.path("account/login");

        Credentials credentials = new Credentials();
        credentials.setEmail("test@gmail.com");
        credentials.setPassword("test");

        token = loginTarget.request(MediaType.TEXT_PLAIN)
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON))
                .readEntity(String.class);

        WebTarget removeTarget = target.path("account");

        Response response = removeTarget.request()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .delete();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response.close();
    }

    @AfterClass
    public static void tearDown() {

        server.shutdown();
    }
}
