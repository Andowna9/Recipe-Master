package com.codecooks;

import com.codecooks.serialize.Credentials;
import com.codecooks.serialize.RegistrationData;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

// TESTING SERVER OVERALL RESPONSE
@PerfTest
public class APIPerfTest {

    private static Logger log = Logger.getLogger(APIPerfTest.class);
    @Rule public ContiPerfRule rule = new ContiPerfRule();

    private static HttpServer server;
    private static WebTarget target;
    private static String token;

    // Testing objects
    private static RegistrationData reg1;
    private static Credentials credentials;


    @BeforeClass
    public static void setUp() {
        log.info("STARTING TEST SETUP");

        server = Main.startServer();
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);

    }

    @BeforeClass
    public static void prepareObjects() {
        log.info("PERFORMING INITIAL OBJECT SETUP");

        reg1 = new RegistrationData();
        reg1.setUsername("Test");
        reg1.setEmail("test@gmail.com");
        reg1.setPassword("test");

        credentials = new Credentials();
        credentials.setEmail("test@gmail.com");
        credentials.setPassword("test");

    }

    // ACCOUNT RESOURCE
    @Test
    public void testSVRegistration() {

        WebTarget registerTarget = target.path("account");

        Response response = registerTarget.request()
                .post(Entity.entity(reg1, MediaType.APPLICATION_JSON));

        response.close();
    }

    @Test
    public void testSVLogin() {

        WebTarget loginTarget = target.path("account/login");

        Response response = loginTarget.request(MediaType.TEXT_PLAIN)
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));

        token = response.readEntity(String.class);
        response.close();

    }

    @Test
    public void testSVLogout() {

        WebTarget loginTarget = target.path("account/login");

        token = loginTarget.request(MediaType.TEXT_PLAIN)
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON))
                .readEntity(String.class);

        WebTarget removeTarget = target.path("account");

        Response response = removeTarget.request()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .delete();

        response.close();

    }

    @AfterClass
    public static void tearDown() {
        log.info("TESTS COMPLETED, PERFORMING CLEANUP");

        server.shutdown();

    }
}
