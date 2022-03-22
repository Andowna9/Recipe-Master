package com.codecooks.authentication;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

/**
 * Authentication filter to process client token.
 * @see <a href="https://developer.mozilla.org/es/docs/Web/HTTP/Authentication">Http Authentication</a>
 */
@Authenticate
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final String AUTHENTICATION_SCHEMA = "Bearer"; // Token based authentication
    private static final String REALM = "Access to recipe-master API";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (!isAuthorizationValid(authorizationHeader)) {

            abortWithUnauthorized(requestContext);
            return; // End filtering
        }

        String token = authorizationHeader.substring(AUTHENTICATION_SCHEMA.length()).trim();
        if (!TokenManager.getInstance().isTokenValid(token)) {
            abortWithUnauthorized(requestContext);
        }

    }

    private boolean isAuthorizationValid(String authorizationHeader) {

        return authorizationHeader != null && authorizationHeader.toLowerCase().startsWith(AUTHENTICATION_SCHEMA.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEMA + " realm=\"" + REALM + "\"")
                .build());
    }
}
