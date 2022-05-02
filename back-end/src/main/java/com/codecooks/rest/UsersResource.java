package com.codecooks.rest;

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

@Path("/users")
public class UsersResource {

    private UserDAO userDAO = new UserDAO();

    // Get personal profile
    @GET @Path("/me")
    @Authenticate
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfile(@Context SecurityContext securityContext) {

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);

        // Created recipes
        List<RecipeBriefData> postedRecipeBriefs = new ArrayList<>();
        for (Recipe recipe: user.getPostedRecipes()) {

            RecipeBriefData recipeBriefData = new RecipeBriefData();
            recipeBriefData.setId(recipe.getId());
            recipeBriefData.setTitle(recipe.getTitle());
            postedRecipeBriefs.add(recipeBriefData);
        }

        // Favourite recipes
        List<RecipeBriefData> favouriteRecipeBriefs = new ArrayList<>();
        for (Recipe recipe: user.getFavouriteRecipes()) {

            RecipeBriefData recipeBriefData = new RecipeBriefData();
            recipeBriefData.setId(recipe.getId());
            recipeBriefData.setTitle(recipe.getTitle());
            favouriteRecipeBriefs.add(recipeBriefData);
        }

        ProfileData profileData = new ProfileData();
        profileData.setUsername(username);
        profileData.setCookingExperience(user.getCookingExp());
        profileData.setCountryCode(user.getCountryCode());
        profileData.setPostedRecipeBriefs(postedRecipeBriefs);
        profileData.setFavouriteRecipeBriefs(favouriteRecipeBriefs);

        return Response.ok().entity(profileData).build();

    }

    // Get profile edition data
    @GET @Path("/me/edit")
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
        data.setAboutMe(user.getAboutMe());

        return Response.ok().entity(data).build();
    }

    // Update profile
    @PUT @Path("/me/edit")
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
        user.setAboutMe(data.getAboutMe());

        userDAO.save(user);

        return Response.ok().build();
    }

    // Search profile
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchProfile(@QueryParam("username") String username) {

        List<User> users = userDAO.searchByText("username", username, 5);

        List<String> usernames = new ArrayList<>();
        for (User user: users) {

            usernames.add(user.getUsername());
        }

        return Response.ok().entity(usernames).build();

    }



}