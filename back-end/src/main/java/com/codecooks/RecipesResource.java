package com.codecooks;

import com.codecooks.authentication.Authenticate;
import com.codecooks.dao.RecipeDAO;
import com.codecooks.dao.UserDAO;
import com.codecooks.domain.Recipe;
import com.codecooks.domain.User;
import com.codecooks.serialize.RecipeBriefData;
import com.codecooks.serialize.RecipeData;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Path("/recipes")
public class RecipesResource {

    private static Logger log = Logger.getLogger(RecipesResource.class);
    private RecipeDAO recipeDAO = new RecipeDAO();
    private UserDAO userDAO = new UserDAO();

    // Post recipe
    @POST
    @Authenticate
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext securityContext, RecipeData data) {

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);

        String title = data.getTitle();
        String content = data.getContent();

        Recipe recipe = new Recipe(title, content);
        recipe.setCreator(user);
        user.addRecipePost(recipe);

        recipeDAO.save(recipe);

        log.info("New recipe posted: " + recipe);
        
        return Response.status(Response.Status.CREATED).build();
    }

    // Search recipe
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

    // Get recipe post
    @GET @Path("/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@PathParam("postId") String id) {

        long recipeId = Long.parseLong(id);
        Recipe recipe = recipeDAO.findBy("id", recipeId);

        RecipeData data = new RecipeData();
        data.setTitle(recipe.getTitle());
        data.setContent(recipe.getContent());

        return Response.ok().entity(data).build();
    }

    // Edit recipe
    @POST @Path("/{postId}")
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

    // Delete recipe
    @DELETE @Path("/{postId}")
    public Response deletePost(@PathParam("postId") String id) {

        long recipeId = Long.parseLong(id);
        Recipe recipe = recipeDAO.findBy("id", recipeId);
        recipeDAO.delete(recipe);

        log.info("Recipe deleted: " + recipe);

        return Response.status(Response.Status.OK).build();
    }

    // Add as favourite
    @GET @Path("/{postId}/favourite")
    @Authenticate
    public Response addFavourite(@PathParam("postId") String id, @Context SecurityContext securityContext) {

        long recipeId = Long.parseLong(id);
        Recipe recipe = recipeDAO.findBy("id", recipeId);

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);

        user.addFavouriteRecipe(recipe);
        recipe.addUserLinkedToFav(user);
        userDAO.save(user);

        return Response.ok().build();
    }

}
