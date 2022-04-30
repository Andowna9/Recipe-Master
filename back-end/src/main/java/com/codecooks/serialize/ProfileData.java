package com.codecooks.serialize;

import com.codecooks.domain.CookingExperience;
import java.util.List;

public class ProfileData {

    private String username;
    private CookingExperience cookingExp;
    private String countryCode;
    private List<RecipeBriefData> postedRecipeBriefs;
    private List<RecipeBriefData> favouriteRecipeBriefs;


    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public CookingExperience getCookingExperience() {

        return cookingExp;
    }

    public void setCookingExperience(CookingExperience cookingExp) {

        this.cookingExp = cookingExp;
    }

    public String getCountryCode() {

        return countryCode;
    }

    public void setCountryCode(String countryCode) {

        this.countryCode = countryCode;
    }

    public List<RecipeBriefData> getPostedRecipeBriefs() {
        return postedRecipeBriefs;
    }

    public void setPostedRecipeBriefs(List<RecipeBriefData> postedRecipeBriefs) {
        this.postedRecipeBriefs = postedRecipeBriefs;
    }

    public List<RecipeBriefData> getFavouriteRecipeBriefs() {
        return favouriteRecipeBriefs;
    }

    public void setFavouriteRecipeBriefs(List<RecipeBriefData> favouriteRecipeBriefs) {
        this.favouriteRecipeBriefs = favouriteRecipeBriefs;
    }
}
