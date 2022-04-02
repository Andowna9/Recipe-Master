package com.codecooks;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class MainController {

    @FXML
    private AnchorPane contentAnchor;

    @FXML
    private void loadProfileMenu() throws IOException {
        contentAnchor.getChildren().setAll(App.loadFXML("profile"));

    }

}
