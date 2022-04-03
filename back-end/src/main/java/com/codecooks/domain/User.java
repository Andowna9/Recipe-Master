package com.codecooks.domain;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class that represents a registered app user.
 */
@PersistenceCapable(detachable = "true")
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

    // FK containing id of user that created the recipe
    @Persistent(mappedBy = "creator", defaultFetchGroup = "true")
    private List<Recipe> postedRecipes;

	public User(String username, String email, String password) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.postedRecipes = new ArrayList<>();
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
		return this.gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
    
    public CookingExperience getCookingeExp() {
		return this.cookingeExp;
	}

	public void setCookingeExp(CookingExperience cookingeExp) {
		this.cookingeExp = cookingeExp;
	}

    public List<Recipe> getPostedRecipes() {
        return this.postedRecipes;
    }

    public void addRecipePost(Recipe recipe) {
        this.postedRecipes.add(recipe);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }

    @Override
    public String toString() {

        return "@" + username + " : " + email;
    }
    
}
