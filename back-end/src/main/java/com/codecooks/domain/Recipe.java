package com.codecooks.domain;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Column;
import javax.jdo.listener.DeleteCallback;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class that represents a recipe post.
 */
@PersistenceCapable(detachable = "true")
public class Recipe implements DeleteCallback {

    // Basic attributes
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    @Column(name = "_id")
    private long id;
    private String title;
    private String content;
    private LocalDate date;

    private String countryCode;
    // TODO numLikes and tags

    @Persistent(defaultFetchGroup = "true")
    private User creator;

    @Persistent(defaultFetchGroup = "true", mappedBy = "favouriteRecipes")
    private Set<User> usersLinkedToFav;

    public Recipe(String title, String content, String countryCode) {
        this.title = title;
        this.content = content;
        this.countryCode = countryCode;
        this.date = LocalDate.now();
        this.usersLinkedToFav = new HashSet<>();
    }

    public long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String newContent) {
        this.content = newContent;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public User getCreator() {
        return this.creator;
    }

    public void setCreator(User user) {
        this.creator = user;
    }

    public void addUserLinkedToFav(User user) {

        usersLinkedToFav.add(user);
    }

    public void removeUserLinkedToFav(User user) {

        usersLinkedToFav.remove(user);
    }

    public int getNumUsersLinkedToFav() {

        return usersLinkedToFav.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return id == recipe.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Recipe #" + id + " *" + title + "*";
    }

    @Override
    public void jdoPreDelete() {

        for (User user: usersLinkedToFav) {

            user.removeFavouriteRecipe(this);
        }
    }
}
