package com.codecooks.domain;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Column;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Class that represents a recipe post.
 */
@PersistenceCapable(detachable = "true")
public class Recipe {

    // Basic attributes
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.INCREMENT)
    @Column(name = "_id")
    private long id;
    private String title;
    private String content;
    private LocalDate date;

    // TODO Add country, numLikes and tags

    public Recipe(String title, String content) {
        this.title = title;
        this.content = content;
        this.date = LocalDate.now();
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

    public LocalDate getDate() {
        return this.date;
    }

    public long getId() {
        return id;
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
}
