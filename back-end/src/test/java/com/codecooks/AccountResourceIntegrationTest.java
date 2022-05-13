package com.codecooks;

import com.codecooks.authentication.SessionManager;
import com.codecooks.dao.UserDAO;
import com.codecooks.domain.User;
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

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Integration Test for Account Resource API endpoint.
 */
public class AccountResourceIntegrationTest {

    private static HttpServer server;
    private static WebTarget target;
    private static String token;

    private static final UserDAO userDAO = new UserDAO();
    private User user;

    @BeforeClass
    public static void setUp() {

        server = Main.startServer();
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);
    }

    @Before
    public void prepareData() {

        user = new User("test", "test@gmail.com", "test");
        userDAO.save(user);

        token = SessionManager.getInstance().generateToken();
        SessionManager.getInstance().startSession(token, "test");

    }

    // Register
    @Test
    public void testRegister() {

        // Delete user fixture to test register as a new one
        userDAO.delete(user);

        WebTarget registerTarget = target.path("account");

        RegistrationData data = new RegistrationData();
        data.setUsername("test");
        data.setEmail("test@gmail.com");
        data.setPassword("test");

        Response response = registerTarget.request()
                            .post(Entity.entity(data, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response.close();

    }

    // Login
    @Test
    public void testLogin() {

        WebTarget loginTarget = target.path("account/login");

        Credentials credentials = new Credentials();
        credentials.setEmail("test@gmail.com");
        credentials.setPassword("test");

        Response response = loginTarget.request(MediaType.TEXT_PLAIN)
                            .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        token = response.readEntity(String.class);
        assertNotNull(token);
    }

    // Logout
   @Test
    public void testLogout() {

        WebTarget logoutTarget = target.path("account/login");

        Response response = logoutTarget.request()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .delete();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response.close();
    }

    // Remove
    @Test
    public void testDeleteAccount() {

        WebTarget removeTarget = target.path("account");

        Response response = removeTarget.request()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .delete();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response.close();
    }

    @After
    public void removeData() {

        userDAO.deleteAll();
        SessionManager.getInstance().endSession("test");
    }

    @AfterClass
    public static void tearDown() {

        server.shutdown();
    }
}
