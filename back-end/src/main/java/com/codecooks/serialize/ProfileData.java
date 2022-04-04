package com.codecooks.serialize;

import java.util.List;

public class ProfileData {

    String username;
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
