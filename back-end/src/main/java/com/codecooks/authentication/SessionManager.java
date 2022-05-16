package com.codecooks.authentication;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;

/**
 * Class containing user token utilities.
 * It implements the singleton pattern (eager initialization).
 */
public class SessionManager {

    private static final SessionManager INSTANCE = new SessionManager();
    private HashMap<String, String> sessions = new HashMap<>(); // Token : Username

    private SessionManager() { }

    public static SessionManager getInstance() {

        return INSTANCE;
    }

    /**
     * Generates a random token to use as authentication.
     * @return generated token
     */
    public String generateToken() {

        Random random = new SecureRandom();
        String token = new BigInteger(130, random).toString(32);

        return token;
    }

    /**
     * Checks if the provided token belongs to a session.
     * @param token token
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token) {

        return sessions.containsKey(token);
    }

    /**
     * Gets username linked to a session.
     * @param token session token
     * @return username, null if token is not valid
     */
    public String getUsername(String token) {

        return sessions.get(token);
    }

    /**
     * Starts a new user session.
     * @param token token
     * @param username username
     */
    public void startSession(String token, String username) {

        sessions.put(token, username);

    }

    /**
     * Ends an existing user session.
     * @param username username
     */
    public void endSession(String username) {

        sessions.values().remove(username);
    }
}
