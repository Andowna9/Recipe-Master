package com.codecooks.authentication;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;

/**
 * Class containing user token utilities.
 * It implements the singleton pattern (eager initialization).
 */
public class TokenManager {

    private static final TokenManager INSTANCE = new TokenManager();
    private HashMap<String, String> sessions = new HashMap<>();

    private TokenManager() { }

    public static TokenManager getInstance() {

        return INSTANCE;
    }

    public String generateToken() {

        Random random = new SecureRandom();
        String token = new BigInteger(130, random).toString(32);

        // TODO Save token for a user to keep the session

        return token;
    }

    public boolean isTokenValid(String token) {

        return sessions.containsKey(token);
    }

    public String getUsername(String token) {

        return sessions.get(token);
    }
}
