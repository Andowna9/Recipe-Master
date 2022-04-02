package com.codecooks;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RecipeShowingController implements Initializable {

    // CONTENT
    private int recipeID;
    public void setRecipeID(int id) { this.recipeID = id; } // TODO

    //JAVAFX

    @FXML private Label lRecipeContent;
    @FXML private Label lRecipeName;


    // METHODS
    @FXML
    private void goBack() {
        try {
            App.goBack();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // DEFAULTS, override later please
        String recipeName = "404 - Recipe not found";
        String recipeContent = "Content could not be loaded. If you are seeing this message, is probably because the programmers did something wrong.";

        // GET THE INFORMATION ABOUT THE RECIPE HERE AND OVERRIDE THE VARIABLES IF INFORMATION WAS RECOVERED
        // REST API call

        lRecipeName.setText(recipeName);
        lRecipeContent.setText(recipeContent);

    }
}
