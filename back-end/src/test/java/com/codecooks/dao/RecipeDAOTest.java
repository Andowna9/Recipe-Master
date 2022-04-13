package com.codecooks.dao;

import com.codecooks.domain.Recipe;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class RecipeDAOTest {

    private static RecipeDAO recipeDAO;
    private static Recipe testRecipe1;
    private static Recipe testRecipe2;
    private static Recipe testRecipe3;

    // Fixtures creation
    @BeforeClass
    public static void setUp() {

        recipeDAO = new RecipeDAO();

        testRecipe1 = new Recipe("Test recipe 1", "Test content");
        testRecipe2 = new Recipe("Test recipe 2", "Test content");
        testRecipe3 = new Recipe("Test recipe 2", "Test content");

        recipeDAO.save(testRecipe1);
        recipeDAO.save(testRecipe2);
        recipeDAO.save(testRecipe3);
    }

    // Testing queries
    @Test
    public void testExists() {

        boolean recipeExits = recipeDAO.exists(String.format("id == %s", testRecipe1.getId()));
        assertTrue(recipeExits);

    }

    @Test
    public void testFindBy() {

        Recipe dbRecipe = recipeDAO.findBy("id", testRecipe2.getId());
        assertNotNull(dbRecipe);
        assertEquals(testRecipe2, dbRecipe);

    }

    @Test
    public void testSearchByTitle() {

        long limit = 2;
        List<Recipe> dbRecipes = recipeDAO.searchByText("title", "Test", limit);
        assertEquals(limit, dbRecipes.size());
        assertFalse(dbRecipes.contains(testRecipe3));

        dbRecipes = recipeDAO.searchByText("title", "Testjsad", limit);
        assertEquals(0, dbRecipes.size());
    }

    //Fixtures deletion
    @AfterClass
    public static void tearDown() {

        recipeDAO.delete(testRecipe1);
        recipeDAO.delete(testRecipe2);
        recipeDAO.delete(testRecipe3);
    }
}
