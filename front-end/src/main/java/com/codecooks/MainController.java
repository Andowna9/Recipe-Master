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
    @FXML private TextField tfSearchBar;


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
    private void launchSearch() {
        String searchInput = tfSearchBar.getText();
        System.out.println("Search: " + searchInput);

        WebTarget target = ServerConnection.getInstance().getTarget("recipes/id/1");
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            System.out.println(response.readEntity(RecipeData.class).getTitle());
        }

        else {

            System.out.println(response.getStatus());
        }
    }

}
