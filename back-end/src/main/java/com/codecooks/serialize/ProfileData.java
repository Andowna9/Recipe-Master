package com.codecooks.serialize;

import com.codecooks.domain.CookingExperience;
import com.codecooks.domain.Gender;

import java.time.LocalDate;
import java.util.List;

public class ProfileData {

    private String username;
    List<RecipeBriefData> recipeBriefDataList;

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public List<RecipeBriefData> getRecipeBriefData() {
        return recipeBriefDataList;
    }

    public void setRecipeBriefData(List<RecipeBriefData> recipeBriefDataList) {
        this.recipeBriefDataList = recipeBriefDataList;
    }
}
