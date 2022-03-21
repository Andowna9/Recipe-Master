package com.codecooks.domain;

import javax.jdo.annotations.PersistenceCapable;
import java.time.LocalDate;

/**
 * Class that represents a recipe post.
 */
@PersistenceCapable
public class Recipe {

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

}
