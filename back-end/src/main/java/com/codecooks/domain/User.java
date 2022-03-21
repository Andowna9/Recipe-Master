package com.codecooks.domain;

import javax.jdo.annotations.PersistenceCapable;
import java.time.LocalDate;

/**
 * Class that represents a registered app user.
 */
@PersistenceCapable
public class User {

    // Basic attributes for authentication
    private String username;
    private String email;
    private String password;

    // Extra attributes
    private String name;
    private String surname;
    private String aboutMe;
    private LocalDate birthDate;
    // TODO Add country, cooking experience and gender

    public User(String username, String email, String password) {

        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }
}
