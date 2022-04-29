package com.codecooks;

import com.codecooks.serialize.RecipeData;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class RecipesModifyingController implements Initializable {

    public enum Mode {

        CREATION, EDITION
    }

    @FXML private Label lView;
    @FXML private TextField tfRecipeTitle;
    @FXML private TextArea taRecipeContent;
    @FXML private ComboBox<String> cbCountryPick;
    @FXML private Button btnAccept;

    private CountryManager countryManager;

    private long recipeId;
    private Mode mode;

    private WebTarget target;

    public RecipesModifyingController() {

        countryManager = new CountryManager();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cbCountryPick.getItems().setAll(countryManager.getCountryNames());

        if (mode == Mode.CREATION) {

            target = ServerConnection.getInstance().getTarget("recipes");

            lView.setText(resourceBundle.getString("label.lView"));
            btnAccept.setText(resourceBundle.getString("button.post"));

            btnAccept.setOnAction(actionEvent -> {

                makeRequest(Response.Status.CREATED);
            });

        }

        else if (mode == Mode.EDITION) {

            target = ServerConnection.getInstance().getTarget("recipes/id/" + recipeId);

            Response response = target.request(MediaType.APPLICATION_JSON).get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {

                RecipeData data = response.readEntity(RecipeData.class);
                tfRecipeTitle.setText(data.getTitle());
                taRecipeContent.setText(data.getContent());
                if (data.getCountryCode() != null) cbCountryPick.setValue(countryManager.getNameFromCode(data.getCountryCode()));
            }

            lView.setText(resourceBundle.getString("label.lView2"));
            btnAccept.setText(resourceBundle.getString("button.save"));

            btnAccept.setOnAction(actionEvent -> {

                makeRequest(Response.Status.OK);
            });

        }

    }

    private void makeRequest(Response.Status expected) {

        RecipeData data = new RecipeData();
        data.setTitle(tfRecipeTitle.getText());
        data.setContent(taRecipeContent.getText());
        data.setCountryCode(countryManager.getCodeFromName(cbCountryPick.getValue()));

        Response response = target.request().post(Entity.entity(data, MediaType.APPLICATION_JSON));

        if (response.getStatus() == expected.getStatusCode()) {

            goBack();
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

    public void setRecipeId(long recipeId) {

        this.recipeId = recipeId;
    }

    public void setMode(Mode mode) {

        this.mode = mode;
    }
}
