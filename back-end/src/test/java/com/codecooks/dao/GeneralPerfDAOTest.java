package com.codecooks.dao;

import com.codecooks.domain.Recipe;
import com.codecooks.domain.User;
import com.codecooks.rest.RecipesResource;
import org.apache.log4j.Logger;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

// DEFAULTS GO ASSIGNED TO THE CLASS!
@PerfTest
public class GeneralPerfDAOTest {

    private static Logger log = Logger.getLogger(GeneralPerfDAOTest.class);
    @Rule public ContiPerfRule rule = new ContiPerfRule();

    private static UserDAO userDAO;
    private static RecipeDAO recipeDAO;

    private static User u1, u2, u3;
    private static User users[] = new User[3];
    private static Recipe r1, r2, r3;
    private static Recipe recipes[] = new Recipe[3];

    @BeforeClass
    public static void setUp() {
        log.info( "TESTS ARE GETTING SET UP " );

        userDAO = new UserDAO();
        recipeDAO = new RecipeDAO();

        u1 = new User("tu_morenito19", "zorman@email.com", "1m");
        u2 = new User("amama", "amama@consorcioDeAbuelas.es", "verySecure");
        u3 = new User("dontHackMe", "louis1997@hotmail.com", "password");

        userDAO.save(u1); userDAO.save(u2); userDAO.save(u3);
        users[0] = u1; users[1] = u2; users[2] = u3;

        r1 = new Recipe("Flan de huevo casero", "This is my content", "UK");
        r2 = new Recipe("Ultimate fruit milkshake with a lot of calcium", "Is this my content?", "ES");
        r3 = new Recipe("Drama", "To contain or not to contain, that's the question", "ES");

        recipeDAO.save(r1); recipeDAO.save(r2); recipeDAO.save(r3);
        recipes[0] = r1; recipes[1] = r2; recipes[2] = r2;

        log.info("TESTS READY.");
    }

    @Test
    @PerfTest(threads = 20, duration = 20000)
    public void stressDAO() {
        log.info("RUNNING HEAVY TEST");

        int random = (int) (Math.random() * 3 + 1);
        String randomUser = users[random-1].getUsername();

        userDAO.searchByText("username", randomUser, random);
        recipeDAO.findBy("id", recipes[random-1].getId() );

        recipeDAO.findAll();

    }

    public void tearDown() {
        log.info("PERFORMING PROCEDURAL CLEANUP");

        userDAO.delete(u1); userDAO.delete(u2); userDAO.delete(u3);
        recipeDAO.delete(r1); recipeDAO.delete(r2); recipeDAO.delete(r3);

    }

}
