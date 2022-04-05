package com.codecooks;

import com.codecooks.authentication.Authenticate;
import com.codecooks.dao.UserDAO;
import com.codecooks.domain.Recipe;
import com.codecooks.domain.User;
import com.codecooks.serialize.ProfileData;
import com.codecooks.serialize.RecipeBriefData;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.ArrayList;
import java.util.List;

@Path("/profile")
public class ProfileResource {

    private UserDAO userDAO = new UserDAO();

    @GET
    @Authenticate
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfile(@Context SecurityContext securityContext) {

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);

        List<RecipeBriefData> recipeBriefDataList = new ArrayList<>();
        for (Recipe recipe: user.getPostedRecipes()) {

            RecipeBriefData recipeBriefData = new RecipeBriefData();
            recipeBriefData.setId(recipe.getId());
            recipeBriefData.setTitle(recipe.getTitle());
            recipeBriefDataList.add(recipeBriefData);
        }


        ProfileData profileData = new ProfileData();
        profileData.setUsername(username);
        profileData.setRecipeBriefData(recipeBriefDataList);

        return Response.ok().entity(profileData).build();

    }

}
