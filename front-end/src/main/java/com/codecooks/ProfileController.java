package com.codecooks;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML
    private Label lUsername;
    @FXML
    private Label lCountry;
    @FXML
    private ImageView userAvatar;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // DEFAULTS, override later please
        String username = "404";
        String country = "UNK";
        Image avatar = new Image(Objects.requireNonNull(App.class.getResourceAsStream("img/Broken_Image.png")));

        // GET THE INFORMATION ABOUT THE USER HERE AND OVERRIDE THE VARIABLES IF INFORMATION WAS RECOVERED
        // call to REST API
        // username = getFromRestAPI

        lUsername.setText(username);
        lCountry.setText(country);
        userAvatar.setImage(avatar);

    }
}
