package com.codecooks;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class MainController {

    @FXML private AnchorPane varScreen;
    @FXML private TextField tfSearchBar;


    @FXML
    private void loadProfileMenu() throws IOException {
        varScreen.getChildren().setAll(App.loadFXML("profile"));

    }
    @FXML
    private void loadHomeMenu() {

    }

    @FXML
    private void loadPostRecipeMenu() {
        try {
            App.setRoot("recipePost");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void launchSearch() {
        String searchInput = tfSearchBar.getText();
        System.out.println("Search: " + searchInput);
    }

}
