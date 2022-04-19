package com.codecooks;

import javafx.fxml.FXML;
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

            RecipesModifyingController controller = new RecipesModifyingController();
            controller.setMode(RecipesModifyingController.Mode.CREATION);
            App.setRoot("recipesModify", controller);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void launchSearch() throws IOException {

        varScreen.getChildren().setAll(App.loadFXML("search").getChildrenUnmodifiable());
    }

}
