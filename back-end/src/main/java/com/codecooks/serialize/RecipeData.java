package com.codecooks.serialize;

public class RecipeData {

    private Long id;
    private String title;
    private String content;
    private String countryCode;
    private boolean isFavourite;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCountryCode() {

        return countryCode;
    }

    public void setCountryCode(String countryCode) {

        this.countryCode = countryCode;
    }

    public boolean getIsFavourite() {

        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {

        this.isFavourite = isFavourite;
    }
}
