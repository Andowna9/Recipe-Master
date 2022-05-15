package com.codecooks.serialize;

public class PopularRecipeFeedData extends RecipeFeedData {

    private int numFavourites;

    public int getNumFavourites() {
        return numFavourites;
    }

    public void setNumFavourites(int numFavourites) {
        this.numFavourites = numFavourites;
    }
}
