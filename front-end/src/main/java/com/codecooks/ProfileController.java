package com.codecooks;

import com.codecooks.serialize.ProfileData;
import com.codecooks.serialize.RecipeBriefData;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {

    @FXML private Label lUsername;
    @FXML private Label lCountry;
    @FXML private ImageView ivUserAvatar;
    @FXML private AnchorPane recipeFeedPanel;

    @FXML private ListView<RecipeBriefData> listView;
    private static ObservableList<RecipeBriefData> recipeObservableList;

    @FXML private Label lRecipeTitle;
    @FXML private HBox hbRecipeContainer;
    // Buttons
    @FXML private Button bShowRecipe;
    @FXML private Button bEditRecipe;
    @FXML private Button bDeleteRecipe;

    public ProfileController() {

        recipeObservableList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // DEFAULTS
        String username = "404";
        String country = "UNK";
        Image avatar = new Image(Objects.requireNonNull(App.class.getResourceAsStream("img/Broken_Image.png")));

        WebTarget target = ServerConnection.getInstance().getTarget("/profile");
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            ProfileData data = response.readEntity(ProfileData.class);
            username = data.getUsername();

            // Adding recipes to list
            recipeObservableList.addAll(data.getRecipeBriefData());
        }

        // Setting the profile values
        lUsername.setText(username);
        lCountry.setText(country);
        ivUserAvatar.setImage(avatar);


        // If there are no recipes posted
        if (recipeObservableList.isEmpty()) {

            listView.setDisable(true);

            HBox hb = new HBox();
            Label lb = new Label("No recipes found");
            lb.setFont(Font.font(22));
            hb.getChildren().add(lb);

            hb.setAlignment( Pos.CENTER );
            AnchorPane.setLeftAnchor(hb, 0.0);
            AnchorPane.setRightAnchor(hb, 0.0);
            AnchorPane.setTopAnchor(hb, 0.0);
            AnchorPane.setBottomAnchor(hb, 0.0);

            recipeFeedPanel.getChildren().add(hb);

        }

        else {

            listView.setItems(recipeObservableList);
            listView.setCellFactory(recipeListView -> new RecipeListViewCell(this));
            listView.setMouseTransparent(false);
            listView.setFocusTraversable(false);
        }




    }

    @FXML
    private void profileConfigMenu() throws IOException {
        App.setRoot("editProfile");
    }

    protected void displayRecipe(long id) throws IOException {

        RecipeShowingController controller = new RecipeShowingController();
        controller.setRecipeId(id);
        App.setRoot("recipeShow", controller);
    }

    protected void editRecipe(long id) throws IOException {

        RecipeEditingController controller = new RecipeEditingController();
        controller.setRecipeId(id);
        App.setRoot("recipeEdit", controller);
    }

    protected void deleteRecipe(long id, String name, int cellIndex) {

        // Show confirmation alert
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("You are deleting: " + name);
        a.setHeaderText("You are deleting a recipe!");
        a.showAndWait();

        if (a.getResult() == ButtonType.OK) {

            System.out.println("[INFO] Delete confirmation");
            WebTarget target = ServerConnection.getInstance().getTarget("recipes/id/" + id);
            Response response = target.request().delete();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {

                recipeObservableList.remove(cellIndex);

            }

        } else {
            System.out.println("[INFO] Delete cancelled");
        }
    }

}


class RecipeListViewCell extends ListCell<RecipeBriefData> {

    @FXML private Label lRecipeTitle;
    @FXML private HBox hbRecipeContainer;
    // Buttons
    @FXML private Button bShowRecipe;
    @FXML private Button bEditRecipe;
    @FXML private Button bDeleteRecipe;

    private ProfileController profileController;

    public RecipeListViewCell(ProfileController profileController) {

        this.profileController = profileController;

    }

    @Override
    protected void updateItem(RecipeBriefData recipe, boolean empty) {
        super.updateItem(recipe, empty);

        if (empty || recipe == null) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader fml = new FXMLLoader(App.class.getResource( "recipeContainer.fxml"));
            fml.setController(this);

            try{
                fml.load();
            } catch (IOException e) {
                System.err.println("[ERR ] CONTAINER COULD NOT BE LOADED");
            }

            lRecipeTitle.setText( recipe.getTitle() );

            // ADDING A LISTENER TO EACH BUTTON
            bShowRecipe.setOnAction( actionEvent -> {
                try {
                    profileController.displayRecipe( recipe.getId() );
                } catch (IOException e) {
                    System.err.println("[ERR0] Error while trying to swap to recipe view");
                }
            } );

            bEditRecipe.setOnAction( actionEvent -> {
                try {
                    profileController.editRecipe( recipe.getId() );
                } catch (IOException e) {
                    System.err.println("[ERR0] Error while trying to swap to recipe edit view");
                }
            } );

            bDeleteRecipe.setOnAction( actionEvent -> { profileController.deleteRecipe( recipe.getId(), recipe.getTitle(), getIndex() );});


            // ADDING THE CONTENTS TO THE LIST
            setText(null);
            setGraphic(hbRecipeContainer);
        }


    }

}