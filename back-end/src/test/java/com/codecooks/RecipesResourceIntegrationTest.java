package com.codecooks;

import com.codecooks.authentication.SessionManager;
import com.codecooks.dao.RecipeDAO;
import com.codecooks.dao.UserDAO;
import com.codecooks.domain.Recipe;
import com.codecooks.domain.User;

import com.codecooks.serialize.RecipeData;
import com.codecooks.serialize.RecipeBriefData;
import com.codecooks.serialize.PopularRecipeFeedData;
import com.codecooks.serialize.RecentRecipeFeedData;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Integration Test for Recipe Resource API endpoint.
 */
public class RecipesResourceIntegrationTest {

    private static HttpServer server;
    private static WebTarget target;
    private static String token;

    private final UserDAO userDAO = new UserDAO();
    private User user;

    private final RecipeDAO recipeDAO = new RecipeDAO();
    private List<Recipe> recipes;

    @BeforeClass
    public static void setUp() {

        server = Main.startServer();
        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);

        token = SessionManager.getInstance().generateToken();
        SessionManager.getInstance().startSession(token, "test");

    }

    @Before
    public void prepareData() throws InterruptedException {

        user = new User("test", "test@gmail.com", "5678");
        userDAO.save(user);

        Recipe recipe1 = new Recipe("Test recipe 1", "Test content 1", "ES");
        recipe1.setCreator(user);
        recipeDAO.save(recipe1);

        Thread.sleep(5);

        Recipe recipe2 = new Recipe("Test recipe 2", "Test content 2", "UK");
        recipe2.setCreator(user);
        recipeDAO.save(recipe2);

        Thread.sleep(5);

        Recipe recipe3 = new Recipe("Test recipe 3", "Test content 3", "FR");
        recipe3.setCreator(user);
        recipeDAO.save(recipe3);


        recipes = new ArrayList<>();
        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);
    }

    @Test
    public void testPostRecipe() {

        WebTarget postTarget = target.path("recipes");

        RecipeData recipeData = new RecipeData();
        recipeData.setTitle("Post test");
        recipeData.setContent("Posting new recipe!");
        recipeData.setCountryCode("UK");

        Response response = postTarget.request()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .post(Entity.entity(recipeData, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        response.close();

        List<Recipe> dbRecipes = recipeDAO.findAll();
        assertEquals(recipes.size() + 1, dbRecipes.size());

    }

    @Test
    public void testGetRecipe() {

        Recipe recipe = recipes.get(0);
        WebTarget getTarget = target.path("recipes/" + recipe.getId());

        Response response = getTarget.request(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        RecipeData recipeData = response.readEntity(RecipeData.class);
        assertNotNull(recipeData);

        assertEquals(recipe.getTitle(), recipeData.getTitle());
        assertEquals(recipe.getContent(), recipeData.getContent());
        assertEquals(recipe.getCountryCode(), recipeData.getCountryCode());
        assertEquals(recipe.getCreator().getUsername(), recipeData.getAuthorUsername());

    }

    @Test
    public void testSearchRecipe() {

        Recipe recipe = recipes.get(0);
        WebTarget searchTarget = target.path("recipes");

        Response response = searchTarget.queryParam("title", recipe.getTitle().substring(0,2))
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<RecipeBriefData> searchResults = response.readEntity(new GenericType<List<RecipeBriefData>>() {});
        assertNotNull(searchResults);

        assertTrue(searchResults.stream().anyMatch(brief -> brief.getId() == recipe.getId()));

    }

    @Test
    public void testEditRecipe() {

        Recipe recipe = recipes.get(0);
        WebTarget editTarget = target.path("recipes/" + recipe.getId());

        String modTitle = "Modified title";
        String modContent = "Modified title";
        String modCountryCode = "AR";

        RecipeData recipeData = new RecipeData();
        recipeData.setTitle(modTitle);
        recipeData.setContent(modContent);
        recipeData.setCountryCode(modCountryCode);

        Response response = editTarget.request()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .put(Entity.entity(recipeData, MediaType.APPLICATION_JSON));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response.close();

        Recipe dbRecipe = recipeDAO.findBy("id", recipe.getId());
        assertEquals(modTitle, dbRecipe.getTitle());
        assertEquals(modContent, dbRecipe.getContent());
        assertNotEquals(modCountryCode, dbRecipe.getCountryCode()); //Not allowed

    }

    @Test
    public void testDeleteRecipe() {

        Recipe recipe = recipes.get(0);
        WebTarget delTarget = target.path("recipes/" + recipe.getId());

        Response response = delTarget.request()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .delete();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response.close();

        List<Recipe> dbRecipes = recipeDAO.findAll();
        assertEquals(recipes.size() - 1, dbRecipes.size());
        assertFalse(dbRecipes.contains(recipe));

    }

    @Test
    public void testAddFavouriteRecipe() {

        Recipe recipe = recipes.get(0);
        WebTarget favTarget = target.path("recipes/" + recipe.getId() + "/favourite");

        Response response = favTarget.request()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        User dbUser = userDAO.findBy("username", user.getUsername());
        assertEquals(1, dbUser.getFavouriteRecipes().size());
        assertTrue(dbUser.getFavouriteRecipes().contains(recipe));

    }

    @Test
    public void testDeleteFavouriteRecipe() {

        Recipe recipe = recipes.get(0);
        recipe.addUserLinkedToFav(user);
        recipeDAO.save(recipe);

        WebTarget favTarget = target.path("recipes/" + recipe.getId() + "/favourite");

        Response response = favTarget.request()
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .delete();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response.close();

        User dbUser = userDAO.findBy("username", user.getUsername());
        assertEquals(0, dbUser.getFavouriteRecipes().size());
    }

    @Test
    public void testGetPopularRecipes() {

        User favUser = new User("fav", "fav@gmail.com", "aSask");
        userDAO.save(favUser);

        // Recipe 1 -> 0 stars

        // Recipe 2 -> 1 stars

        Recipe recipe2 = recipes.get(1);
        recipe2.addUserLinkedToFav(favUser);
        recipeDAO.save(recipe2);

        // Recipe 3 -> 2 stars

        Recipe recipe3 = recipes.get(2);
        recipe3.addUserLinkedToFav(user);
        recipe3.addUserLinkedToFav(favUser);
        recipeDAO.save(recipe3);

        WebTarget popularTarget = target.path("recipes/popular");

        Response response = popularTarget.request(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<PopularRecipeFeedData> results = response.readEntity(new GenericType<List<PopularRecipeFeedData>>() {});
        assertNotNull(results);

        Collections.reverse(recipes);
        assertTrue(IntStream.range(0, results.size()).allMatch(index -> results.get(index).getId() == recipes.get(index).getId()));

        assertEquals(recipes.get(0).getNumUsersLinkedToFav(), results.get(0).getNumFavourites());
        assertEquals(recipes.get(1).getNumUsersLinkedToFav(), results.get(1).getNumFavourites());
        assertEquals(recipes.get(2).getNumUsersLinkedToFav(), results.get(2).getNumFavourites());


    }

    @Test
    public void testGetRecentRecipes() {

        WebTarget recentTarget = target.path("recipes/recent");

        Response response = recentTarget.request(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                            .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<RecentRecipeFeedData> results = response.readEntity(new GenericType<List<RecentRecipeFeedData>>() {});
        assertNotNull(results);

        // Reverse recipes order since recent are the last created
        Collections.reverse(recipes);
        assertTrue(IntStream.range(0, results.size()).allMatch(index -> results.get(index).getId() == recipes.get(index).getId()));

    }

    @After
    public void removeData() {

        userDAO.deleteAll();
        recipeDAO.deleteAll();

    }


    @AfterClass
    public static void tearDown() {

        SessionManager.getInstance().endSession("test");
        server.shutdown();
    }
}
