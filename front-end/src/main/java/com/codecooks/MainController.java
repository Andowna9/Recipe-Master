package com.codecooks;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public enum SubView { HOME, PROFILE, SEARCH }

    @FXML private AnchorPane varScreen;

    private SubView subView = SubView.HOME;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        switch (subView) {

            case HOME:
                loadHomeMenu();
                break;

            case PROFILE:
                loadProfileMenu();
                break;

            case SEARCH:
                loadSearchMenu();
                break;
        }
    }

    @FXML
    private void loadProfileMenu() {
        try {
            varScreen.getChildren().setAll(App.loadFXML("profile").getChildrenUnmodifiable());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    private void loadHomeMenu() {

        // CHANGE WHEN HOME VIEW IS READY
        varScreen.getChildren().setAll(new Pane());
    }

    @FXML
    private void loadSearchMenu() {

        try {
            varScreen.getChildren().setAll(App.loadFXML("search").getChildrenUnmodifiable());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSubView(SubView subView) {

        this.subView = subView;
    }

}
