package com.codecooks;

import com.codecooks.serialize.RecipeData;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RecipeShowingController implements Initializable {

    private long recipeId;

    //JAVAFX

    @FXML private Label lRecipeTitle;
    @FXML private Label lRecipeContent;


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

        // DEFAULTS
        lRecipeTitle.setText("404 - Recipe not found");
        lRecipeContent.setText("Content could not be loaded. If you are seeing this message, is probably because the programmers did something wrong.");

        // REST API call
        WebTarget target = ServerConnection.getInstance().getTarget("recipes/id/" + recipeId);
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            RecipeData data = response.readEntity(RecipeData.class);

            lRecipeTitle.setText(data.getTitle());
            lRecipeContent.setText(data.getContent());

        }

    }

    public void setRecipeId(long recipeId) {

        this.recipeId = recipeId;
    }
}
