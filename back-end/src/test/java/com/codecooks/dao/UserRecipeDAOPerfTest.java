package com.codecooks.dao;

import com.codecooks.domain.Recipe;
import com.codecooks.domain.User;
import com.codecooks.rest.RecipesResource;
import org.apache.log4j.Logger;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

@PerfTest
@Required(max=1000, average=500) // A maximum of 1s execution (per method)
public class UserRecipeDAOPerfTest {

    // Logger setup
    private static Logger log = Logger.getLogger(UserRecipeDAOPerfTest.class);

    private static UserDAO userDAO;
    private static RecipeDAO recipeDAO;

    private static User user1, user2, user3;
    private static Recipe recipe1, recipe2, recipe3;

    // Necessary for tests to work
    @Rule public ContiPerfRule rule = new ContiPerfRule();

    @BeforeClass
    public static void setUp() {
        log.info( "SETTING UP TESTS" );

        userDAO = new UserDAO();
        recipeDAO = new RecipeDAO();

        user1 = new User("user1", "user1@gmail.com", "1234");
        user2 = new User("user2", "user2@gmail.com", "1234");
        user3 = new User("user3", "user3@gmail.com", "1234");

        userDAO.save(user1);
        userDAO.save(user2);
        userDAO.save(user3);

        recipe1 = new Recipe("Recipe 1", "Content 1", "ES");
        recipe2 = new Recipe("Recipe 2", "Content 2", "ES");
        recipe3 = new Recipe("Recipe 3", "Content 3", "ES");

        recipeDAO.save(recipe1);
        recipeDAO.save(recipe2);
        recipeDAO.save(recipe3);

        log.info("SETUP DONE");
    }

    @Test
    @PerfTest(invocations = 5)
    public void testAddFavouriteRecipe() {
        log.info( "RUNNING FAVORITE RECIPE PERF TEST" );

        user1.addFavouriteRecipe(recipe1);
        recipe1.addUserLinkedToFav(user1);

        userDAO.save(user1);

        Recipe dbRecipe = recipeDAO.findBy("id", recipe1.getId());
        User dbUser = userDAO.findBy("username", user1.getUsername());

    }

    @AfterClass
    public static void tearDown() {
        log.info( "TESTS DONE, CLEANING UP" );

        userDAO.delete(user1);
        userDAO.delete(user2);
        userDAO.delete(user3);

        recipeDAO.delete(recipe1);
        recipeDAO.delete(recipe2);
        recipeDAO.delete(recipe3);

    }
}
