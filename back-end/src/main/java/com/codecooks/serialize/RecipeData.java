package com.codecooks.serialize;

public class RecipeData {

    private long id;
    private String title;
    private String content;
    private String countryCode;
    private String authorUsername;

    private boolean isFavourite;
    private int numFavourites;

    public long getId() {
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

    public boolean isFavourite() {

        return isFavourite;
    }

    public void setFavourite(boolean isFavourite) {

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
