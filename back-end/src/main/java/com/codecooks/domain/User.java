package com.codecooks.domain;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.listener.DeleteLifecycleListener;
import javax.jdo.listener.InstanceLifecycleEvent;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

/**
 * Class that represents a registered app user.
 */
@PersistenceCapable(detachable = "true")
public class User implements DeleteLifecycleListener {

    // Basic attributes for authentication
    private String username;
    private String email;
    private String password;

    // Extra attributes
    private String name;
    private String aboutMe;
    private LocalDate birthDate;
    private String countryCode;
    private Gender gender;
    private CookingExperience cookingExp;

    private String avatarLocation;

    // FK containing id of user that created the recipe
    @Persistent(mappedBy = "creator", defaultFetchGroup = "true")
    private List<Recipe> postedRecipes;

    @Persistent(defaultFetchGroup = "true")
    private Set<Recipe> favouriteRecipes;

	public User(String username, String email, String password) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.postedRecipes = new ArrayList<>();
        this.favouriteRecipes = new HashSet<>();
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

    public void setName(String name) {

        this.name = name;
    }

    public String getName() {

        return this.name;
    }

    public void setAboutMe(String newAboutMe) {

        this.aboutMe = newAboutMe;
    }

    public String getAboutMe() {

        return this.aboutMe;
    }

	public Gender getGender() {
		return this.gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

    public String getCountryCode() {

        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {

        this.countryCode = countryCode;
    }

    public void setBirthDate(LocalDate birthDate) {

        this.birthDate = birthDate;
    }

    public LocalDate getBirthDate() {

        return this.birthDate;
    }
    
    public CookingExperience getCookingExp() {
		return this.cookingExp;
	}

	public void setCookingExp(CookingExperience cookingExp) {
		this.cookingExp = cookingExp;
	}

    public List<Recipe> getPostedRecipes() {
        return this.postedRecipes;
    }

    public void addRecipePost(Recipe recipe) {
        this.postedRecipes.add(recipe);
    }

    public void addFavouriteRecipe(Recipe recipe) {

        favouriteRecipes.add(recipe);
    }

    public void removeFavouriteRecipe(Recipe recipe) {

        favouriteRecipes.remove(recipe);
    }

    public List<Recipe> getFavouriteRecipes() {
        return new ArrayList<>(favouriteRecipes);
    }

    public String getAvatarLocation() {
        return avatarLocation;
    }

    public void setAvatarLocation(String avatarLocation) {
        this.avatarLocation = avatarLocation;
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

    // Reference cleaning
    @Override
    public void preDelete(InstanceLifecycleEvent instanceLifecycleEvent) {

        for (Recipe recipe: favouriteRecipes) {
            recipe.removeUserLinkedToFav(this);
        }

    }

    // Removing avatar from filesystem
    @Override
    public void postDelete(InstanceLifecycleEvent instanceLifecycleEvent) {

        File avatarFile = new File(this.avatarLocation);
        if (avatarFile.exists()) avatarFile.delete();
    }
}
