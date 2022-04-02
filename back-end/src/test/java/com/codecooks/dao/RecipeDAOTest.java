package com.codecooks.dao;

import com.codecooks.domain.Recipe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecipeDAOTest {

    private RecipeDAO recipeDAO;
    private Recipe testRecipe;

    @Before
    public void setUp() {

        recipeDAO = new RecipeDAO();
        testRecipe = new Recipe("Test recipe", "Test content");
        recipeDAO.save(testRecipe);
    }

    @Test
    public void testExists() {

        boolean recipeExits = recipeDAO.exists(String.format("id == %s", testRecipe.getId()));
        assertTrue(recipeExits);

    }

    @Test
    public void testFindBy() {

        Recipe dbRecipe = recipeDAO.findBy("id", testRecipe.getId());
        assertNotNull(dbRecipe);
        assertEquals(testRecipe, dbRecipe);

    }

    @After
    public void tearDown() {

        recipeDAO.delete(testRecipe);
    }
}
