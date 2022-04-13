package com.codecooks;

import com.codecooks.serialize.RecipeData;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RecipePostingController {

    @FXML private TextField tfRecipeTitle;
    @FXML private TextArea taRecipeContent;

    @FXML
    private void postContent() {

        RecipeData data = new RecipeData();
        data.setTitle(tfRecipeTitle.getText());
        data.setContent(taRecipeContent.getText());

        WebTarget target = ServerConnection.getInstance().getTarget("recipes");
        Response response = target.request().post(Entity.entity(data, MediaType.APPLICATION_JSON));

        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {

            try {
                App.goBack();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Could not post recipe!");
            App.showAlertAndWait(alert);
        }

    }

    @FXML
    private void goBack() {
        try {
            App.goBack();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
