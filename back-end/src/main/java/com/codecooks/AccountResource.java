package com.codecooks;

import com.codecooks.authentication.Authenticate;
import com.codecooks.authentication.TokenManager;
import com.codecooks.dao.UserDAO;
import com.codecooks.domain.User;
import com.codecooks.serialize.Credentials;
import com.codecooks.serialize.RegistrationData;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.apache.log4j.Logger;

@Path("/account")
public class AccountResource {

    private static Logger log = Logger.getLogger(AccountResource.class.getName());
    private UserDAO userDAO = new UserDAO();

    // Log into account
    @POST @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(Credentials credentials) {

        String email = credentials.getEmail();
        String password = credentials.getPassword();

        User user = userDAO.getBy("email", email);
        if (user != null && user.getPassword().equals(password)) {


            String token = TokenManager.getInstance().generateToken();
            String username = user.getUsername();
            TokenManager.getInstance().startSession(token, username);
            log.debug("Token generated for [" + username + "]: " + token);

            return Response.ok().entity(token).build();
        }


        return Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE,"Basic realm=\"Logging into user account\"")
                .build();
    }

    // Register account
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(RegistrationData data) {

        // Email already exists
        String email = data.getEmail();
        if (userDAO.exists("email == '" + email + "'")) {

            return Response.status(Response.Status.CONFLICT).build();

        }

        // Username is already in use
        String username = data.getUsername();
        if (userDAO.exists("username == '" + username + "'")) {

            return Response.status(Response.Status.CONFLICT).build();
        }

        User user = new User(
                username,
                email,
                data.getPassword()
        );

        userDAO.save(user);
        log.info("New user registered: " + user);

        return Response.status(Response.Status.OK).build();
    }

    // Log out
    @DELETE @Path("/login")
    @Authenticate
    public Response logout(@Context SecurityContext securityContext) {

        String username = securityContext.getUserPrincipal().getName();

        // Delete entry from token manager
        TokenManager.getInstance().endSession(username);
        log.info("User logged out: " + username);

        return Response.status(Response.Status.OK).build();
    }

    // Delete user account
    @DELETE
    @Authenticate
    public Response deleteAccount(@Context SecurityContext securityContext) {

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.getBy("username", username);

        userDAO.delete(user);
        TokenManager.getInstance().endSession(username);
        log.info("User account removed: " + user);

        return Response.status(Response.Status.OK).build();
    }
}
