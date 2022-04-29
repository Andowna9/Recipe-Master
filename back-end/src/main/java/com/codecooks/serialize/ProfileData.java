package com.codecooks.serialize;

import com.codecooks.domain.CookingExperience;
import java.util.List;

public class ProfileData {

    private String username;
    private CookingExperience cookingExp;
    private String countryCode;
    List<RecipeBriefData> recipeBriefDataList;

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

    public List<RecipeBriefData> getRecipeBriefData() {
        return recipeBriefDataList;
    }

    public void setRecipeBriefData(List<RecipeBriefData> recipeBriefDataList) {
        this.recipeBriefDataList = recipeBriefDataList;
    }
}
