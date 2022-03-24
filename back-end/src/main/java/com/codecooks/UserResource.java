package com.codecooks;

import com.codecooks.authentication.Authenticate;
import com.codecooks.authentication.TokenManager;
import com.codecooks.dao.UserDAO;
import com.codecooks.domain.User;
import com.codecooks.serialize.Credentials;
import com.codecooks.serialize.RegistrationData;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.apache.log4j.Logger;

@Path("/user")
public class UserResource {

    private static Logger log = Logger.getLogger(UserResource.class.getName());
    private UserDAO userDAO = new UserDAO();

    @POST @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String login(Credentials credentials){

        String email = credentials.getEmail();
        String password = credentials.getPassword();
        String token = TokenManager.getInstance().generateToken();

        //Usar los DAOs para guardar usuario
        //Condicionales...
        return token;
    }

    @POST @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response register(RegistrationData data) {

        // TODO Check if account already exists

        User user = new User(
                data.getUsername(),
                data.getEmail(),
                data.getPassword(),
                data.getBirthDate()
        );

        userDAO.save(user);
        log.info("New user registered: " + user);

        return Response.status(Response.Status.OK).build();
    }

    @POST @Path("/logout")
    @Authenticate
    public Response logout(@Context SecurityContext securityContext) {

        String username = securityContext.getUserPrincipal().getName();
        // Delete token from manager
        return Response.status(Response.Status.OK).build();
    }
}
