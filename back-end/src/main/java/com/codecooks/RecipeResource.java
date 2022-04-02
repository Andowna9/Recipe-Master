package com.codecooks;

import com.codecooks.authentication.Authenticate;
import com.codecooks.dao.RecipeDAO;
import com.codecooks.domain.Recipe;
import com.codecooks.serialize.RecipeData;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.apache.log4j.Logger;



@Path("/recipes")
public class RecipeResource {
    private static Logger log = Logger.getLogger(AccountResource.class);
    private RecipeDAO recipeDAO = new RecipeDAO();

    // Post recipe
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response post(RecipeData data){
        String title = data.getTitle();
        String content = data.getContent();

        Recipe recipe = new Recipe(title, content);
        recipeDAO.save(recipe);
        
        return Response.status(Response.Status.CREATED).build();
    }

    // Edit recipe
    @POST @Path ("/id/{postId}")
    public Response editPost(@PathParam("postId") String id, RecipeData data){
        Recipe recipe = recipeDAO.getBy("id", id);

        String title = data.getTitle();
        String content = data.getContent();
        recipe.setTitle(title);
        recipe.setContent(content);

        return Response.status(Response.Status.OK).build();
    }

    // Delete recipe
    @DELETE @Path ("/id/{postId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response deletePost(@PathParam("postId") String id){
        Recipe recipe = recipeDAO.getBy("id", id);
        recipeDAO.delete(recipe);
        return Response.status(Response.Status.OK).build();
    }

}
