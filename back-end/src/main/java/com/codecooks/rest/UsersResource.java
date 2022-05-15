package com.codecooks.rest;

import com.codecooks.authentication.Authenticate;
import com.codecooks.dao.UserDAO;
import com.codecooks.domain.Recipe;
import com.codecooks.domain.User;
import com.codecooks.serialize.ProfileData;
import com.codecooks.serialize.ProfileEditionData;
import com.codecooks.serialize.RecipeBriefData;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/users")
public class UsersResource {

    private UserDAO userDAO = new UserDAO();

    // Get personal profile
    @GET @Path("/me")
    @Authenticate
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyProfile(@Context SecurityContext securityContext) {

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

    // Get any user's profile
    @GET @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserProfile(@PathParam("username") String username) {

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
        data.setUsername(user.getUsername());
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

    @POST @Path("/me/avatar")
    @Authenticate
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadAvatarImage(@FormDataParam("file")InputStream fileInputStream,
                                      @FormDataParam("file")FormDataContentDisposition fileMetaData,
                                      @Context SecurityContext securityContext) {

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);

        final String UPLOAD_PATH ="server-files/avatars/";
        String fileName = new Date().getTime() + "-" + fileMetaData.getFileName();

        int read;
        byte [] bytes = new byte[1024];

        try {

            // Create avatars directory if it does not exist
            File directory = new File(UPLOAD_PATH);
            if (!directory.exists()) directory.mkdirs();

            // Write file to filesystem
            String filePath = UPLOAD_PATH + fileName;
            OutputStream out = Files.newOutputStream(Paths.get(filePath));
            while ((read = fileInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            out.flush();
            out.close();

            // Remove previous avatar file
            String currentAvatarLocation = user.getAvatarLocation();
            if (currentAvatarLocation != null) {
                File avatarFile = new File(currentAvatarLocation);
                if (avatarFile.exists()) avatarFile.delete();
            }

            // Update database accordingly
            user.setAvatarLocation(filePath);
            userDAO.save(user);

        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }

        return Response.ok().build();

    }

    @GET @Path("/me/avatar")
    @Authenticate
    @Produces({"image/png", "image/jpg"})
    public Response getAvatarImage(@Context SecurityContext securityContext) {

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);
        String filePath = user.getAvatarLocation();

        File imageFile = new File(filePath);
        if (!imageFile.exists()) return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(imageFile).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + imageFile.getName()).build();

    }


}
