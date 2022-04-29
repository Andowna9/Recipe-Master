package com.codecooks.dao;

import com.codecooks.domain.Recipe;
import com.codecooks.domain.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserRecipeDAOTest {

    private static UserDAO userDAO;
    private static RecipeDAO recipeDAO;

    private static User user1;
    private static User user2;
    private static User user3;

    private static Recipe recipe1;
    private static Recipe recipe2;
    private static Recipe recipe3;

    // Fixtures creation
    @BeforeClass
    public static void setUp() {

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

    }

    @Test
    public void testAddFavouriteRecipe() {

        user1.addFavouriteRecipe(recipe1);
        recipe1.addUserLinkedToFav(user1);

        userDAO.save(user1);

        Recipe dbRecipe = recipeDAO.findBy("id", recipe1.getId());
        User dbUser = userDAO.findBy("username", user1.getUsername());

        assertEquals(1, dbRecipe.getNumUsersLinkedToFav());

        assertEquals(1, dbUser.getFavouriteRecipes().size());
        assertEquals(recipe1, dbUser.getFavouriteRecipes().get(0));

    }

    // Fixtures deletion
    @AfterClass
    public static void tearDown() {

        userDAO.delete(user1);
        userDAO.delete(user2);
        userDAO.delete(user3);

        recipeDAO.delete(recipe1);
        recipeDAO.delete(recipe2);
        recipeDAO.delete(recipe3);

    }
}
