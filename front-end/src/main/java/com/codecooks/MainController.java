package com.codecooks;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
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
            ProfileController controller = new ProfileController(ProfileController.Mode.OWN);
            varScreen.getChildren().setAll(App.loadFXML("profile", controller).getChildrenUnmodifiable());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void loadOtherProfileMenu(String username) {
        ProfileController controller = new ProfileController(username);
        try {
            varScreen.getChildren().setAll(App.loadFXML("profile", controller).getChildrenUnmodifiable());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void loadHomeMenu() {

        // CHANGE WHEN HOME VIEW IS READY
        try {
            varScreen.getChildren().setAll(App.loadFXML("mainFeed").getChildrenUnmodifiable());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadSearchMenu() {

        try {
            varScreen.getChildren().setAll(App.loadFXML("search").getChildrenUnmodifiable());
            SearchController.setParentController(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setSubView(SubView subView) {

        this.subView = subView;
    }

}
