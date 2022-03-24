package com.codecooks;

import com.codecooks.dao.UserDAO;
import com.codecooks.serialize.Credentials;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.log4j.Logger;

@Path("/user")
public class UserResource {

    private static Logger log = Logger.getLogger(UserResource.class.getName());
    private UserDAO userDAO = new UserDAO();

    @POST @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String login(Credentials credentials){
        String token = "test";
        //Usar los DAOs para guardar usuario
        //Condicionales...
        return token;
    }

    @POST @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response register (Credentials credentials){

        return Response.status(Response.Status.OK).build(); //Condicionales. Si okay -> Status.Ok, si no Okay -> Status.Fail?
    }

    @POST @Path("/logout")
    public Response logOut (Credentials credentials){

        return Response.status(Response.Status.OK).build();
    }
}
