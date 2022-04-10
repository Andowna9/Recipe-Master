package com.codecooks;

import com.codecooks.authentication.Authenticate;
import com.codecooks.dao.UserDAO;
import com.codecooks.domain.Recipe;
import com.codecooks.domain.User;
import com.codecooks.serialize.ProfileData;
import com.codecooks.serialize.ProfileEditionData;
import com.codecooks.serialize.RecipeBriefData;
import jakarta.ws.rs.*;
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

    @GET @Path("/edit")
    @Authenticate
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfileEdition(@Context SecurityContext securityContext) {

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);

        ProfileEditionData data = new ProfileEditionData();
        data.setName(user.getName());
        data.setBirthDate(user.getBirthDate());
        data.setCountryCode(user.getCountryCode());
        data.setGender(user.getGender());
        data.setCookingExp(user.getCookingExp());

        return Response.ok().entity(data).build();
    }

    @POST @Path("/edit")
    @Authenticate
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editProfile(@Context SecurityContext securityContext, ProfileEditionData data) {

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);

        user.setName(data.getName());
        user.setBirthDate(data.getBirthDate());
        user.setCountryCode(data.getCountryCode());
        user.setGender(data.getGender());
        user.setCookingExp(data.getCookingExp());

        userDAO.save(user);

        return Response.ok().build();
    }



}
