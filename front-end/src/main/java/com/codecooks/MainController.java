package com.codecooks;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

public class MainController {

    @FXML
    private AnchorPane contentAnchor;

    @FXML
    private void loadProfileMenu() throws IOException {
        contentAnchor.getChildren().setAll(App.loadFXML("profile"));

    }
}
