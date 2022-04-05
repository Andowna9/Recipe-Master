package com.codecooks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProfileEditionController implements Initializable {

    private ObservableList<CookExp> cookingExpOptions =
            FXCollections.observableArrayList(CookExp.Professional, CookExp.Expert, CookExp.Intermediate, CookExp.Amateur, CookExp.Beginner);
    private ObservableList<Gender> genderOptions =
            FXCollections.observableArrayList(Gender.Female, Gender.Male, Gender.Other);
    private ObservableList<Country> countryOptions =
            FXCollections.observableArrayList(Country.Bilbao, Country.Turkey, Country.Spain);

    @FXML private ChoiceBox<CookExp> cbCookingExp;
    @FXML private ChoiceBox<Gender> cbGender;
    @FXML private ChoiceBox<Country> cbCountry;
    @FXML private DatePicker dpBirthDate;
    @FXML private ImageView ivUserAvatar;
    @FXML private Label lUsername;
    @FXML private TextField lName;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cbCookingExp.setItems(cookingExpOptions);
        cbGender.setItems(genderOptions);
        cbCountry.setItems(countryOptions);
    }

    @FXML
    private void goBack() throws IOException {
            App.goBack();
    }

    @FXML
    private void saveChanges() {
        // TODO
        String name = lName.getText();
        String cookingExp = cbCookingExp.getValue().toString();
        String gender = cbGender.getValue().toString();
        String country = cbCountry.getValue().toString();
        LocalDate bd = dpBirthDate.getValue();

        // User.setCookingExp( cookingExp )
    }
}

enum CookExp {
    Professional("Professional"),
    Expert("Expert"),
    Intermediate("Intermediate"),
    Amateur("Amateur"),
    Beginner("Beginner");

    public String name;

    CookExp(String n) { name = n; }
    public String toString() { return name; }
}

enum Gender {
    Female("Female"),
    Male("Male"),
    Other("Other");

    private String name;

    Gender(String g) { name = g; }
    public String toString() { return name; }

}

enum Country {
    Bilbao("Bilbao"),
    Turkey("Turkey"),
    Spain("Spain");

    private String country;

    Country(String c) { country = c; }
    public String toString() { return country; }
}

