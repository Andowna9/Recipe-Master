package com.codecooks.serialize;

import com.codecooks.domain.CookingExperience;
import com.codecooks.domain.Gender;

import java.time.LocalDate;

public class ProfileEditionData {

    private String name;
    private LocalDate birthDate;
    private Gender gender;
    private CookingExperience cookingExp;

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

    public CookingExperience getCookingExp() {
        return cookingExp;
    }

    public void setCookingExp(CookingExperience cookingExperience) {
        this.cookingExp = cookingExperience;
    }
}
