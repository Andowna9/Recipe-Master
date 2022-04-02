package com.codecooks;

import com.codecooks.dao.RecipeDAO;
import com.codecooks.domain.Recipe;
import com.codecooks.serialize.RecipeData;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.apache.log4j.Logger;

@Path("/recipes")
public class RecipesResource {

    private static Logger log = Logger.getLogger(RecipesResource.class);
    private RecipeDAO recipeDAO = new RecipeDAO();

    // Post recipe
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(RecipeData data) {

        String title = data.getTitle();
        String content = data.getContent();

        Recipe recipe = new Recipe(title, content);
        recipeDAO.save(recipe);

        log.info("New recipe posted: " + recipe);
        
        return Response.status(Response.Status.CREATED).build();
    }

    @GET @Path("id/{postId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPost(@PathParam("postId") String id) {

        Recipe recipe = recipeDAO.findBy("id", id);

        RecipeData data = new RecipeData();
        data.setTitle(recipe.getTitle());
        data.setContent(recipe.getContent());

        return Response.ok().entity(data).build();
    }

    // Edit recipe
    @POST @Path ("/id/{postId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editPost(@PathParam("postId") String id, RecipeData data) {
        Recipe recipe = recipeDAO.findBy("id", id);

        String title = data.getTitle();
        String content = data.getContent();
        recipe.setTitle(title);
        recipe.setContent(content);

        return Response.status(Response.Status.OK).build();
    }

    // Delete recipe
    @DELETE @Path ("/id/{postId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePost(@PathParam("postId") String id) {

        Recipe recipe = recipeDAO.findBy("id", id);
        recipeDAO.delete(recipe);

        log.info("Recipe deleted: " + recipe);

        return Response.status(Response.Status.OK).build();
    }

}
