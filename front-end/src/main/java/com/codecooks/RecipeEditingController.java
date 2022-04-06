package com.codecooks;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RecipeEditingController implements Initializable {

    @FXML private TextField tfRecipeTitle;
    @FXML private TextArea taRecipeContent;

    @FXML
    private void goBack() throws IOException {
        App.goBack();
    }

    @FXML
    private void postContent() {
        // TODO REST API UPDATE CONTENT
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String previousTitle = "Previous name of the recipe";
        String previousContent = "Previous content of the recipe";

        // TODO REST API communication
        // IF CONTENT WAS FOUND
        // previousTitle = getFromRestApi();
        // previousContent = getFromRestApi()

        tfRecipeTitle.setText(previousTitle);
        taRecipeContent.setText(previousContent);
    }
}
