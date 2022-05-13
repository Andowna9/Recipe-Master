package com.codecooks.serialize;

import com.codecooks.domain.CookingExperience;
import com.codecooks.domain.Gender;

import java.time.LocalDate;

public class ProfileEditionData {

    private String username;
    private String name;
    private LocalDate birthDate;
    private String countryCode;
    private Gender gender;
    private CookingExperience cookingExp;
    private String aboutMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public CookingExperience getCookingExp() {
        return cookingExp;
    }

    public void setCookingExp(CookingExperience cookingExperience) {
        this.cookingExp = cookingExperience;
    }

    public String getAboutMe() {

        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {

        this.aboutMe = aboutMe;
    }
}
