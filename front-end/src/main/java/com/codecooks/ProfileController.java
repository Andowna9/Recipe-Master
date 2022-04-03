package com.codecooks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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

    @FXML private ListView<Recipe> listView;
    private ObservableList<Recipe> recipeObservableList;

    public ProfileController() {

        recipeObservableList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // DEFAULTS, override later please
        String username = "404";
        String country = "UNK";
        Image avatar = new Image(Objects.requireNonNull(App.class.getResourceAsStream("img/Broken_Image.png")));



        // ADD HERE THE RECIPES YOU WANT TO LIST
        recipeObservableList.addAll(
                new Recipe(1, "Recipe test 1"),
                new Recipe(2, "Recipe test 2")
        );

        // GET THE INFORMATION ABOUT THE USER HERE AND OVERRIDE THE VARIABLES IF INFORMATION WAS RECOVERED
        /* username = getFromRestAPI
         *  avatar = getFromRestAPI
         */

        //

        // SETTING THE LIST
        if ( recipeObservableList.isEmpty() ) { // IF WE DON'T FIND ANY ITEM IN THE LIST

            // HIDING THE LIST
            listView.setDisable(true);

            // CREATING A LABEL AND A CONTAINER (TO CENTER THE LABEL)
            HBox hb = new HBox();
            Label lb = new Label("No recipes found");
            lb.setFont(Font.font(22));
            hb.getChildren().add(lb);

            // CENTERING THE CONTAINER TO THE CENTER OF THE ANCHOR PANE
            hb.setAlignment( Pos.CENTER );
            AnchorPane.setLeftAnchor(hb, 0.0);
            AnchorPane.setRightAnchor(hb, 0.0);
            AnchorPane.setTopAnchor(hb, 0.0);
            AnchorPane.setBottomAnchor(hb, 0.0);

            // ADDING THE HBOX TO THE ANCHOR PANE
            recipeFeedPanel.getChildren().add(hb);

        } else { // IF LIST IS NOT EMPTY
            listView.setItems(recipeObservableList);
            listView.setCellFactory( recipeListView -> new RecipeListViewCell() );
            listView.setMouseTransparent(false);
            listView.setFocusTraversable(false);

        }

        // SETTING THE VALUES
        lUsername.setText(username);
        lCountry.setText(country);
        ivUserAvatar.setImage(avatar);

    }
}

class Recipe {
    private int id;
    private String recipeName;

    Recipe(int id, String recipeName) {
        this.id = id;
        this.recipeName = recipeName;
    }

    public String getName() { return recipeName; }
    public int getID() { return id; }

    @Override
    public String toString(){
        return recipeName;
    }
}

class RecipeListViewCell extends ListCell<Recipe>{
    @FXML private Label lRecipeName;
    @FXML private HBox hbRecipeContainer;

    @Override
    protected void updateItem(Recipe recipe, boolean empty) {
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

            lRecipeName.setText( recipe.getName() );

            setText(null);
            setGraphic(hbRecipeContainer);
        }


    }
}