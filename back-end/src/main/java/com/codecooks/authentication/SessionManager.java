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

    public String generateToken() {

        Random random = new SecureRandom();
        String token = new BigInteger(130, random).toString(32);

        return token;
    }

    public boolean isTokenValid(String token) {

        return sessions.containsKey(token);
    }

    public String getUsername(String token) {

        return sessions.get(token);
    }

    public void startSession(String token, String username) {

        sessions.put(token, username);

    }

    public void endSession(String username) {

        sessions.values().remove(username);
    }
}
