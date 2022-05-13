package com.codecooks;

import com.codecooks.serialize.ProfileData;
import com.codecooks.serialize.RecipeBriefData;
import com.codecooks.utils.I18n;
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
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    public enum Mode {
        OWN, OTHER // My profile VS other user's profile
    }

    private Mode mode;
    private String globalUsername = "";

    @FXML private Label lUsername;
    @FXML private Label lCookingExp;
    @FXML private Label lCountry;
    @FXML private ImageView ivUserAvatar;
    @FXML private AnchorPane recipeFeedPanel;

    @FXML private ListView<RecipeBriefData> listView;
    private static ObservableList<RecipeBriefData> recipeObservableList;
    private static ObservableList<RecipeBriefData> favouritesObservableList;

    @FXML private Label lRecipeTitle;
    @FXML private HBox hbRecipeContainer;
    // Buttons
    @FXML private Button bShowRecipe;
    @FXML private Button bEditRecipe;
    @FXML private Button bDeleteRecipe;

    // Buttons that should only display in you own profile
    @FXML private Button bConfigMenu;
    @FXML private Button bCreateRecipe;

    @FXML private FontIcon fiFav;
    @FXML private FontIcon fiProfile;

    public ProfileController(Mode m) {

        recipeObservableList = FXCollections.observableArrayList();
        favouritesObservableList = FXCollections.observableArrayList();
        this.mode = m;
    }

    public ProfileController(String username) {
        this(Mode.OTHER);
        this.setUsername(username);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // DEFAULTS
        String username = "404";
        String country = "UNK";
        String cookingExp = "Undefined";
        Image avatar = new Image(Objects.requireNonNull(App.class.getResourceAsStream("img/Broken_Image.png")));

        WebTarget target;
        if (mode == Mode.OTHER) target = ServerConnection.getInstance().getTarget("users/" + globalUsername);
        else if (mode == Mode.OWN) target = ServerConnection.getInstance().getTarget("users/me");
        else {
            target = ServerConnection.getInstance().getTarget("users/"); // Will return a 404
            logger.error("Mode should be defined before calling the controller. Check the constructors");
        }

        Response response = target.request(MediaType.APPLICATION_JSON).get();


        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            ProfileData data = response.readEntity(ProfileData.class);
            username = data.getUsername();
            if (data.getCountryCode() != null) country = data.getCountryCode();
            if (data.getCookingExperience() != null) cookingExp = I18n.getEnumTranslation(data.getCookingExperience());

            // Adding recipes to list
            recipeObservableList.addAll(data.getPostedRecipeBriefs()); // todo both (show differently) changes not needed at data level
            favouritesObservableList.addAll(data.getFavouriteRecipeBriefs());

        } else {
            logger.warn("Answer from server not OK:"
                    + "\n |_ User: " + globalUsername
                    + "\n |_ Mode: " + mode
                    + "\n |_ Status: " + response.getStatus()
            );
        }

        // Setting the profile values
        lUsername.setText(username);
        lCookingExp.setText(cookingExp);
        lCountry.setText(country);

        ivUserAvatar.setImage(avatar);

        // Default view
        bConfigMenu.setVisible(false);
        bCreateRecipe.setVisible(false);

        if (mode == Mode.OWN) initOwnMode();
        else if (mode == Mode.OTHER) initOtherMode();
        else initOtherMode();

    }

    @FXML private void showFavList() {
        fiProfile.setIconLiteral("ci-user");
        fiFav.setIconLiteral("ci-star-filled");
        listView.setItems(favouritesObservableList);

        // If there are no recipes posted
        if (favouritesObservableList.isEmpty()) {
            listView.setDisable(true);
            HBox hb = genNotFoundHBox();
            recipeFeedPanel.getChildren().add(hb);

        }

        else {
            listView.setItems(favouritesObservableList);
            listView.setCellFactory(recipeListView -> new RecipeListViewCell(this));
            listView.setMouseTransparent(false);
            listView.setFocusTraversable(false);
        }

    }

    @FXML private void showProfileList() {
        fiProfile.setIconLiteral("ci-user-filled");
        fiFav.setIconLiteral("ci-star");
        listView.setItems(recipeObservableList);
        recipeFeedPanel.getChildren().clear();

        // If there are no recipes posted
        if (recipeObservableList.isEmpty()) {
            listView.setDisable(true);
            HBox hb = genNotFoundHBox();
            recipeFeedPanel.getChildren().add(hb);

        }

        else {

            listView.setItems(recipeObservableList);
            listView.setCellFactory(recipeListView -> new RecipeListViewCell(this));
            listView.setMouseTransparent(false);
            listView.setFocusTraversable(false);
            recipeFeedPanel.getChildren().add(listView);
            listView.setDisable(false);
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

        RecipesModifyingController controller = new RecipesModifyingController();
        controller.setMode(RecipesModifyingController.Mode.EDITION);
        controller.setRecipeId(id);
        App.setRoot("recipesModify", controller);
    }

    protected void deleteRecipe(long id, String name, int cellIndex) {

        // Show confirmation alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("You are deleting: " + name);
        alert.setHeaderText("You are deleting a recipe!");
        App.showAlertAndWait(alert);

        if (alert.getResult() == ButtonType.OK) {
            logger.info("Delete confirmation");

            WebTarget target = ServerConnection.getInstance().getTarget("recipes/" + id);
            Response response = target.request().delete();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {

                recipeObservableList.remove(cellIndex);

            }

        } else {
            logger.info("Delete cancelled");
        }
    }

    @FXML
    private void loadPostRecipeMenu() {

        try {
            RecipesModifyingController controller = new RecipesModifyingController();
            controller.setMode(RecipesModifyingController.Mode.CREATION);
            App.setRoot("recipesModify", controller);

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Loading posted recipe menu", e);
        }
    }

    private static HBox genNotFoundHBox() {
        HBox hb = new HBox();
        Label lb = new Label("No recipes found");
        lb.setFont(Font.font(22));
        hb.getChildren().add(lb);

        hb.setAlignment( Pos.CENTER );
        AnchorPane.setLeftAnchor(hb, 0.0);
        AnchorPane.setRightAnchor(hb, 0.0);
        AnchorPane.setTopAnchor(hb, 0.0);
        AnchorPane.setBottomAnchor(hb, 0.0);

        return hb;
    }

    public void setMode(Mode m) { mode = m; }

    private void initOwnMode() {
        bConfigMenu.setVisible(true);
        bCreateRecipe.setVisible(true);
        showProfileList(); // todo only private (we need a different type of lists for public profile)
    }

    private void initOtherMode() {
        // TODO create method for showing recipes alternatively
    }

    public void setUsername(String uname) { this.globalUsername = uname; }

}

class RecipeListViewCell extends ListCell<RecipeBriefData> {

    private static final Logger logger = LoggerFactory.getLogger(RecipeListViewCell.class);

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
                logger.error("CONTAINER COULD NOT BE LOADED", e);
            }

            lRecipeTitle.setText( recipe.getTitle() );

            // ADDING A LISTENER TO EACH BUTTON
            bShowRecipe.setOnAction( actionEvent -> {
                try {
                    profileController.displayRecipe( recipe.getId() );
                } catch (IOException e) {
                    logger.error("Error while trying to swap to recipe view", e);
                }
            } );

            bEditRecipe.setOnAction( actionEvent -> {
                try {
                    profileController.editRecipe( recipe.getId() );
                } catch (IOException e) {
                    logger.error("Error while trying to swap to recipe edit view", e);
                }
            } );

            bDeleteRecipe.setOnAction( actionEvent -> { profileController.deleteRecipe( recipe.getId(), recipe.getTitle(), getIndex() );});


            // ADDING THE CONTENTS TO THE LIST
            setText(null);
            setGraphic(hbRecipeContainer);
        }


    }

}