package com.codecooks;

import com.codecooks.serialize.RecipeData;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class MainController {

    @FXML private AnchorPane varScreen;


    @FXML
    private void loadProfileMenu() throws IOException {
        varScreen.getChildren().setAll(App.loadFXML("profile").getChildrenUnmodifiable());

    }
    @FXML
    private void loadHomeMenu() {

        // CHANGE WHEN HOME VIEW IS READY
        varScreen.getChildren().setAll(new Pane());
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
    private void launchSearch() throws IOException {

        varScreen.getChildren().setAll(App.loadFXML("search").getChildrenUnmodifiable());
    }

}
