package com.codecooks.dao;

import com.codecooks.domain.User;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Unitary Test for accessing users stored in DB.
 */
public class UserDAOTest {

    private static UserDAO userDAO;
    private static User testUser1;
    private static User testUser2;

    // Fixtures creation
    @BeforeClass
    public static void setUp() {

        userDAO = new UserDAO();

        testUser1 = new User("test1", "test1@gmail.com", "1234");
        testUser2 = new User("test2", "test2@gmail.com", "4321");

        userDAO.save(testUser1);
        userDAO.save(testUser2);
    }

    // Testing queries
    @Test
    public void testExists() {

        boolean userExits = userDAO.exists(String.format("username == '%s'", testUser1.getUsername()));
        assertTrue(userExits);

        userExits = userDAO.exists("email == 'something@deusto.es'");
        assertFalse(userExits);

    }

    @Test
    public void testFindBy() {

        User dbUser = userDAO.findBy("username", testUser1.getUsername());
        assertNotNull(dbUser);
        assertEquals(testUser1, dbUser);

        dbUser = userDAO.findBy("email", "lol");
        assertNull(dbUser);
    }

    @Test
    public void testSearchByUsername() {

        List<User> users = userDAO.searchByText("username", "test", 2);
        assertEquals(2, users.size());

        assertEquals(testUser1, users.get(0));
        assertEquals(testUser2, users.get(1));
    }

    // Fixtures deletion
    @AfterClass
    public static void tearDown() {

        userDAO.delete(testUser1);
        userDAO.delete(testUser2);
    }
}
