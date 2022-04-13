package com.codecooks.dao;

import com.codecooks.domain.Recipe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class SearchRecipeDAOTest {

    RecipeDAO recipeDAO;
    Recipe recipe1;
    Recipe recipe2;
    Recipe recipe3;

    @Before
    public void setUp() {

        recipeDAO = new RecipeDAO();

        recipe1 = new Recipe("SearchTest1", "****");
        recipe2 = new Recipe("SearchTest2", "****");
        recipe3 = new Recipe("SearchTest3", "****");

        recipeDAO.save(recipe1);
        recipeDAO.save(recipe2);
        recipeDAO.save(recipe3);
    }

    @Test
    public void testSearchRecipeByTitle() {

        // Check result size
        long limit = 2;
        List<Recipe> recipes = recipeDAO.searchByText("title", "Sea", limit);
        assertEquals(limit, recipes.size());

        // Check no result
        recipes = recipeDAO.searchByText("title", "Search8947", limit);
        assertEquals(0, recipes.size());

    }

    @After
    public void tearDown() {

        recipeDAO.delete(recipe1);
        recipeDAO.delete(recipe2);
        recipeDAO.delete(recipe3);

    }
}
