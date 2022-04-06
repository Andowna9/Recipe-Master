package com.codecooks;

import com.codecooks.serialize.RecipeData;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RecipeEditingController implements Initializable {

    @FXML private TextField tfRecipeTitle;
    @FXML private TextArea taRecipeContent;

    private long recipeId;
    private WebTarget target;

    @FXML
    private void goBack() {
        try {
            App.goBack();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void postContent() {

        RecipeData data = new RecipeData();
        data.setTitle(tfRecipeTitle.getText());
        data.setContent(taRecipeContent.getText());

        Response response= target.request().post(Entity.entity(data, MediaType.APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            try {
                App.goBack();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        target = ServerConnection.getInstance().getTarget("recipes/id/" + recipeId);

        Response response = target.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            RecipeData data = response.readEntity(RecipeData.class);
            tfRecipeTitle.setText(data.getTitle());
            taRecipeContent.setText(data.getContent());
        }

    }

    public void setRecipeId(long recipeId) {

        this.recipeId = recipeId;
    }
}
