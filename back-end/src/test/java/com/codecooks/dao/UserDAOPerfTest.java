package com.codecooks.dao;


import com.codecooks.domain.User;
import org.apache.log4j.Logger;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.*;

import java.util.List;

@PerfTest(invocations = 5)
@Required(max=1000, average=500) // A maximum of 1s execution (per method)
public class UserDAOPerfTest {

    // static
    private static Logger log = Logger.getLogger(UserDAOPerfTest.class);
    @Rule public ContiPerfRule rule = new ContiPerfRule();

    private static UserDAO userDAO;
    private static User testUser1, testUser2;


    @BeforeClass
    public static void setUp() {
        log.info( "SETTING UP TEST" );

        userDAO = new UserDAO();

        testUser1 = new User("test1", "test1@gmail.com", "1234");
        testUser2 = new User("test2", "test2@gmail.com", "4321");

        userDAO.save(testUser1);
        userDAO.save(testUser2);

        log.info( "SETUP DONE" );
    }

    @Test
    @Required(max = 60, average = 30)
    public void testExists() {
        log.info( "TESTING EXISTENCE METHOD" );

        userDAO.exists(String.format("username == '%s'", testUser1.getUsername()));
        userDAO.exists("email == 'something@deusto.es'");
    }

    @Test
    @Required(max = 40, average = 20)
    public void testFindBy() {
        log.info( "TESTING FINDING BY" );

        userDAO.findBy( "username", testUser1.getUsername() );
        userDAO.findBy("email", "lol");

    }

    @Test
    @Required(max = 100, average = 30)
    public void testSearchByUsername() {
        log.info( "TESTING SEARCH BY USERNAME" );

        List<User> users = userDAO.searchByText("username", "test", 2);
    }

    @AfterClass
    public static void tearDown() {

        log.info( "TESTS DONE, PERFORMING CLEANUP" );

        userDAO.delete(testUser1);
        userDAO.delete(testUser2);
    }



}
