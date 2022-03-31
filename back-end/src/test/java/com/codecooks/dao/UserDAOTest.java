package com.codecooks.dao;

import com.codecooks.domain.User;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * Unitary Test for accessing users stored in DB.
 */
public class UserDAOTest {

    private UserDAO userDAO;
    private User testUser;

    @Before
    public void setUp() {

        userDAO = new UserDAO();
        testUser = new User("test", "test@gmail.com", "1234");
        userDAO.save(testUser);
    }

    @Test
    public void testExists() {

        boolean userExits = userDAO.exists(String.format("username == '%s'", testUser.getUsername()));
        assertTrue(userExits);

        userExits = userDAO.exists("email == 'something@deusto.es'");
        assertFalse(userExits);

    }

    @Test
    public void testGetBy() {

        User dbUser = userDAO.getBy("username", testUser.getUsername());
        assertNotNull(dbUser);
        assertEquals(testUser, dbUser);

        dbUser = userDAO.getBy("email", "lol");
        assertNull(dbUser);
    }

    @After
    public void tearDown() {

        userDAO.delete(testUser);
    }
}
