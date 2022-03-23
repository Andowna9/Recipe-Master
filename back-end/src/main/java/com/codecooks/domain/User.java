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
    // TODO Add country
    private Gender gender;
    private CookingExperience cookingeExp;
    

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

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
    
    public CookingExperience getCookingeExp() {
		return cookingeExp;
	}

	public void setCookingeExp(CookingExperience cookingeExp) {
		this.cookingeExp = cookingeExp;
	}
    
}