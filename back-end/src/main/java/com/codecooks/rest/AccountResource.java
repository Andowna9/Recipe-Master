package com.codecooks.rest;

import com.codecooks.authentication.Authenticate;
import com.codecooks.authentication.SessionManager;
import com.codecooks.dao.UserDAO;
import com.codecooks.domain.User;
import com.codecooks.serialize.Credentials;
import com.codecooks.serialize.RegistrationData;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.apache.log4j.Logger;

/**
 * REST resource class that exposes user accounts.
 */
@Path("/account")
public class AccountResource {

    private static final Logger log = Logger.getLogger(AccountResource.class);
    private UserDAO userDAO = new UserDAO();

    /**
     * Logs user into account by creating a new session.
     * @param credentials login credentials (email and password)
     * @return 200 with session token if account exists, 401 (unauthorized) otherwise
     */
    @POST @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(Credentials credentials) {

        String email = credentials.getEmail();
        String password = credentials.getPassword();

        User user = userDAO.findBy("email", email);
        if (user != null && user.getPassword().equals(password)) {


            String token = SessionManager.getInstance().generateToken();
            String username = user.getUsername();
            SessionManager.getInstance().startSession(token, username);
            log.debug("Token generated for [" + username + "]: " + token);

            return Response.ok().entity(token).build();
        }


        return Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE,"Basic realm=\"Logging into user account\"")
                .build();
    }

    /**
     * Registers new user account.
     * @param data registration data
     * @return 200 if account was registered or 409 (conflict) if username or email exist
     */
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

    /**
     * Logs out user and ends the associated session.
     * @param securityContext user authentication
     * @return 200 if session was ended.
     */
    @DELETE @Path("/login")
    @Authenticate
    public Response logout(@Context SecurityContext securityContext) {

        String username = securityContext.getUserPrincipal().getName();

        // Delete entry from token manager
        SessionManager.getInstance().endSession(username);
        log.info("User logged out: " + username);

        return Response.status(Response.Status.OK).build();
    }

    /**
     * Deletes user account permanently.
     * @param securityContext user authentication
     * @return 200 if account was deleted
     */
    @DELETE
    @Authenticate
    public Response deleteAccount(@Context SecurityContext securityContext) {

        String username = securityContext.getUserPrincipal().getName();
        User user = userDAO.findBy("username", username);

        userDAO.delete(user);
        SessionManager.getInstance().endSession(username);
        log.info("User account removed: " + user);

        return Response.status(Response.Status.OK).build();
    }
}
