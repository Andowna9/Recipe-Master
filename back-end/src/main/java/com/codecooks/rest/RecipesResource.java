package com.codecooks.rest;

import com.codecooks.authentication.Authenticate;
import com.codecooks.dao.RecipeDAO;
import com.codecooks.dao.UserDAO;
import com.codecooks.domain.Recipe;
import com.codecooks.domain.User;
import com.codecooks.serialize.RecipeBriefData;
import com.codecooks.serialize.RecipeData;
import com.codecooks.serialize.PopularRecipeFeedData;
import com.codecooks.serialize.RecentRecipeFeedData;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * REST resource class that exposes recipe posts.
 */
@Path("/recipes")
public class RecipesResource {

    private static final Logger log = Logger.getLogger(RecipesResource.class);
    private RecipeDAO recipeDAO = new RecipeDAO();
    private UserDAO userDAO = new UserDAO();

    /**
     * Creates a new recipe post.
     * @param securityContext user authentication
     * @param data recipe data
     * @return 202 if the post was successfully created
     */
    @POST
    @Authenticate
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext securityContext, RecipeData data) {

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);

        String title = data.getTitle();
        String content = data.getContent();
        String countryCode = data.getCountryCode();

        Recipe recipe = new Recipe(title, content, countryCode);
        recipe.setCreator(user);
        user.addRecipePost(recipe);

        recipeDAO.save(recipe);

        log.info("New recipe posted: " + recipe);
        
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Searches for recipe posts starting with the title characters provided.
     * @param title recipe title
     * @return 202 with list of recipes matching the query
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchRecipe(@QueryParam("title") String title) {

        List<Recipe> recipes = recipeDAO.searchByText("title", title, 5);

        List<RecipeBriefData> recipeBriefList = new ArrayList<>();
        for (Recipe recipe: recipes) {

            RecipeBriefData recipeBrief = new RecipeBriefData();
            recipeBrief.setId(recipe.getId());
            recipeBrief.setTitle(recipe.getTitle());

            recipeBriefList.add(recipeBrief);
        }

        return Response.ok().entity(recipeBriefList).build();

    }

    /**
     * Gets a particular recipe post (detailed).
     * @param id recipe identifier
     * @param securityContext user authentication
     * @return 202 with recipe
     */
    @GET @Path("/{postId}")
    @Authenticate
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@PathParam("postId") String id, @Context SecurityContext securityContext) {

        long recipeId = Long.parseLong(id);
        Recipe recipe = recipeDAO.findBy("id", recipeId);

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);

        RecipeData data = new RecipeData();
        data.setAuthorUsername(user.getUsername());
        data.setTitle(recipe.getTitle());
        data.setContent(recipe.getContent());
        data.setCountryCode(recipe.getCountryCode());
        data.setFavourite(user.getFavouriteRecipes().contains(recipe));
        data.setNumFavourites(recipe.getNumUsersLinkedToFav());

        return Response.ok().entity(data).build();
    }

    /**
     * Updates a posted recipe.
     * @param id recipe identifier
     * @param data recipe data
     * @return 202 if update was successful
     */
    @PUT @Path("/{postId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editPost(@PathParam("postId") String id, RecipeData data) {

        long recipeId = Long.parseLong(id);
        Recipe recipe = recipeDAO.findBy("id", recipeId);

        String title = data.getTitle();
        String content = data.getContent();
        recipe.setTitle(title);
        recipe.setContent(content);

        recipeDAO.save(recipe);

        return Response.status(Response.Status.OK).build();
    }

    /**
     * Deletes a recipe post.
     * @param id recipe identifier
     * @return 202 if deletion was successful
     */
    @DELETE @Path("/{postId}")
    public Response deletePost(@PathParam("postId") String id) {

        long recipeId = Long.parseLong(id);
        Recipe recipe = recipeDAO.findBy("id", recipeId);
        recipeDAO.delete(recipe);

        log.info("Recipe deleted: " + recipe);

        return Response.status(Response.Status.OK).build();
    }

    /**
     * Adds a recipe post as favourite.
     * @param id recipe identifier
     * @param securityContext user authentication
     * @return 202 if recipe was added to favourites successfully
     */
    @GET @Path("/{postId}/favourite")
    @Authenticate
    public Response addFavourite(@PathParam("postId") String id, @Context SecurityContext securityContext) {

        long recipeId = Long.parseLong(id);
        Recipe recipe = recipeDAO.findBy("id", recipeId);

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);

        recipe.addUserLinkedToFav(user);
        user.addFavouriteRecipe(recipe);
        recipeDAO.save(recipe);

        log.info("New favourite recipe for " + username + ": " + recipe);

        return Response.ok().build();
    }

    /**
     * Removes recipe post from favourites.
     * @param id recipe identifier
     * @param securityContext user authentication
     * @return 202 if recipe was successfully removed from favourites
     */
    @DELETE @Path("/{postId}/favourite")
    @Authenticate
    public Response removeFavourite(@PathParam("postId") String id, @Context SecurityContext securityContext) {

        long recipeId = Long.parseLong(id);
        Recipe recipe = recipeDAO.findBy("id", recipeId);

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);

        recipe.removeUserLinkedToFav(user);
        user.removeFavouriteRecipe(recipe);
        recipeDAO.save(recipe);

        log.info("Favourite recipe removed for " + username + ": " + recipe);

        return Response.ok().build();
    }

    /**
     * Gets the most recently created recipe posts.
     * @param securityContext user authentication
     * @return 202 with recent recipe posts
     */
    @GET @Path("/recent")
    @Authenticate
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRecentPosts(@Context SecurityContext securityContext) {

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);

        // TODO Find a better solution directly through database (database query, instead of memory)
        List<Recipe> recipes = recipeDAO.findAll();
        recipes.sort(new Comparator<Recipe>() {
            @Override
            public int compare(Recipe r1, Recipe r2) {
                if (r1.getDateTime().isAfter(r2.getDateTime())) {

                    return 1;
                }

                return -1;
            }
        });

        if (recipes.size() > 10) recipes.subList(10, recipes.size()).clear();

        List<RecentRecipeFeedData> recipeFeeds = new ArrayList<>();
        for (Recipe recipe: recipes) {

            RecentRecipeFeedData recipeFeed = new RecentRecipeFeedData();
            recipeFeed.setId(recipe.getId());
            recipeFeed.setTitle(recipe.getTitle());
            recipeFeed.setAuthor(recipe.getCreator().getUsername());
            recipeFeed.setFavourite(user.getFavouriteRecipes().contains(recipe));
            recipeFeed.setDateTime(recipe.getDateTime());

            recipeFeeds.add(recipeFeed);
        }

        return Response.ok(recipeFeeds).build();
    }

    /**
     * Gets the most popular recipe posts (according to the
     * amount of favourite lists they are in).
     * @param securityContext user authentication
     * @return 202 with popular recipe posts
     */
    @GET @Path("/popular")
    @Authenticate
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPopularPosts(@Context SecurityContext securityContext) {

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);

        List<Recipe> recipes = recipeDAO.findMostPopular(10);
        List<PopularRecipeFeedData> recipeFeeds = new ArrayList<>();
        for (Recipe recipe: recipes) {

            PopularRecipeFeedData recipeFeed = new PopularRecipeFeedData();
            recipeFeed.setId(recipe.getId());
            recipeFeed.setTitle(recipe.getTitle());
            recipeFeed.setAuthor(recipe.getCreator().getUsername());
            recipeFeed.setFavourite(user.getFavouriteRecipes().contains(recipe));
            recipeFeed.setNumFavourites(recipe.getNumUsersLinkedToFav());

            recipeFeeds.add(recipeFeed);
        }

        return Response.ok(recipeFeeds).build();
    }

}
