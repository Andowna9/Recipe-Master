package com.codecooks;

import com.codecooks.authentication.Authenticate;
import com.codecooks.dao.RecipeDAO;
import com.codecooks.dao.UserDAO;
import com.codecooks.domain.Recipe;
import com.codecooks.domain.User;
import com.codecooks.serialize.RecipeData;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.apache.log4j.Logger;

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

    // Get recipe post
    @GET @Path("/id/{postId}")
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
    @POST @Path("/id/{postId}")
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
    @DELETE @Path("/id/{postId}")
    public Response deletePost(@PathParam("postId") String id) {

        long recipeId = Long.parseLong(id);
        Recipe recipe = recipeDAO.findBy("id", recipeId);
        recipeDAO.delete(recipe);

        log.info("Recipe deleted: " + recipe);

        return Response.status(Response.Status.OK).build();
    }

}
