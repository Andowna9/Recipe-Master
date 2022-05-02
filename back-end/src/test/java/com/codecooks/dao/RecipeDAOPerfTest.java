package com.codecooks.dao;

import com.codecooks.domain.Recipe;
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
@Required(max = 1000, average = 500)
public class RecipeDAOPerfTest {

    // Logger setup
    private static Logger log = Logger.getLogger(RecipesResource.class);

    private static RecipeDAO recipeDAO;
    private static Recipe testRecipe1, testRecipe2, testRecipe3;

    @Rule public ContiPerfRule rule = new ContiPerfRule();


    @BeforeClass
    public static void setUp() {
        log.info("PERFORMING TEST SETUP");

        recipeDAO = new RecipeDAO();

        testRecipe1 = new Recipe("Test recipe 1", "Test content", "ES");
        testRecipe2 = new Recipe("Test recipe 2", "Test content", "ES");
        testRecipe3 = new Recipe("Test recipe 2", "Test content", "ES");

        recipeDAO.save(testRecipe1);
        recipeDAO.save(testRecipe2);
        recipeDAO.save(testRecipe3);

        log.info("TEST SETUP DONE");
    }

    @Test
    public void testExists() {
        log.info( "TESTING EXISTENCE METHOD" );

        recipeDAO.exists(String.format("id == %s", testRecipe1.getId()));

    }

    @Test
    public void testFindBy() {
        log.info( "TESTING FINDING BY" );

        recipeDAO.findBy("id", testRecipe2.getId());
    }

    @Test
    public void testSearchByTitle() {
        log.info( "TESTING SEARCH BY TITLE" );

        long limit = 2;
        recipeDAO.searchByText("title", "Test", limit);
        recipeDAO.searchByText("title", "HotComputer", limit);

    }

    @AfterClass
    public static void tearDown() {
        log.info( "TESTS DONE, PERFORMING CLEANUP" );

        recipeDAO.delete(testRecipe1);
        recipeDAO.delete(testRecipe2);
        recipeDAO.delete(testRecipe3);

    }
}
