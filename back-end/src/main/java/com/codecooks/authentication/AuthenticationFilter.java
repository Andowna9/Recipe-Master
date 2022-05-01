package com.codecooks.authentication;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;

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
    public void filter(ContainerRequestContext requestContext) {

        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Invalid Authorization Header
        if (!isAuthorizationValid(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        // Invalid token
        String token = authorizationHeader.substring(AUTHENTICATION_SCHEMA.length()).trim();
        if (!SessionManager.getInstance().isTokenValid(token)) {
            abortWithUnauthorized(requestContext);
            return;
        }

        String username = SessionManager.getInstance().getUsername(token);

        // Override security context to inject it on API methods that require identification
        SecurityContext currentSecurityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new UserPrincipal() {
                    @Override
                    public String getName() {
                        return username;
                    }
                };
            }

            @Override
            public boolean isUserInRole(String s) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return AUTHENTICATION_SCHEMA;
            }
        });



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
