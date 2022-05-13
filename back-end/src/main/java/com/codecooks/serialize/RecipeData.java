package com.codecooks.serialize;

public class RecipeData {

    private Long id;
    private String title;
    private String content;
    private String countryCode;
    private String authorUsername;

    private boolean isFavourite;
    private int numFavourites;

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

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public int getNumFavourites() {
        return numFavourites;
    }

    public void setNumFavourites(int numFavourites) {
        this.numFavourites = numFavourites;
    }
}
