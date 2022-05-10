package com.codecooks;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class MainFeedController implements Initializable {

    @FXML private HBox hbPopRecipes;
    @FXML private AnchorPane apPopRecipes;
    @FXML private HBox hbRecentRecipes;
    @FXML private AnchorPane apRecentRecipes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int amtOfPopRecipes = 0;
        int amtOfRecentRecipes = 0;
        int recipeDisplayLimit = 10; // don't change please
        int recipeWidth = 175; int separation = 10; int rightMargin = 20; // don't touch this

        amtOfPopRecipes = 3; // TODO get the amount of popular recipes you want to display.
        amtOfRecentRecipes = 5; // TODO get the amount of recent recipes you want to display.


        if (amtOfPopRecipes > 0) {
            for (int i = 1; i <= amtOfPopRecipes && i <= recipeDisplayLimit; i++) {

                VBox vb = new FeedRecipeContainer(i).getContent(); // TODO get the ID of the recipe
                hbPopRecipes.getChildren().add(vb);

            }

            hbPopRecipes.setPrefWidth( amtOfPopRecipes*recipeWidth + amtOfPopRecipes*separation + rightMargin);
            apPopRecipes.setPrefWidth( hbPopRecipes.getPrefWidth() );

        } else {
            hbPopRecipes.getChildren().add( new Label("No recipes found") );
        }

        if (amtOfRecentRecipes > 0) {
            for (int i = 1; i <= amtOfRecentRecipes && i <= recipeDisplayLimit; i++) {

                VBox vb = new FeedRecipeContainer(i + 10).getContent(); // TODO get the ID of the recipe
                hbRecentRecipes.getChildren().add(vb);

            }

            hbRecentRecipes.setPrefWidth( amtOfRecentRecipes*recipeWidth + amtOfRecentRecipes*separation + rightMargin);
            apRecentRecipes.setPrefWidth( hbRecentRecipes.getPrefWidth() );

        } else {
            hbRecentRecipes.getChildren().add( new Label("No recipes found") );
        }

    }
}

class FeedRecipeContainer {

    private int recipeID;
    private boolean isFav;
    @FXML private VBox vb;
    @FXML private FontIcon favIcon;

    FeedRecipeContainer(int id) {
        this.recipeID = id;

        // INLINE CSS
        vb = new VBox();
        vb.setStyle("-fx-border-style: solid; -fx-border-color: grey; -fx-background-color: #eaeaea");
        vb.setPadding(new Insets(5,5,5,5));
        vb.setSpacing(5); vb.setPrefWidth(175); vb.setPrefHeight(85);

        // LABELS
        Label lTitle = new Label("Recipe title: " + recipeID); // TODO REMOVE
        lTitle.setStyle("-fx-font-size: 16");
        Label lAuthor = new Label("Author");

        // BUTTON MENU (BUTTONS AND ICONS)
        HBox buttonBox = new HBox();
        Button bOpen= new Button(); Button bFav = new Button();
        FontIcon viewIcon = new FontIcon(); viewIcon.setIconLiteral("ci-view"); viewIcon.setIconSize(16);
        favIcon= new FontIcon(); favIcon.setIconSize(16);
        isFav = false; // TODO get if it was already favorited
        updateFavIcon();

        bOpen.setGraphic(viewIcon); bFav.setGraphic(favIcon);
        buttonBox.setSpacing(5);
        buttonBox.getChildren().addAll(bOpen, bFav);

        // BUTTON LISTENERS
        bOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // TODO show recipe
                System.out.println( "ID: " + getID() );
            }
        });

        bFav.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // TODO add to favorites
                swapFav();
            }
        });

        // FINAL STEP
        vb.getChildren().addAll(
                lTitle, lAuthor, buttonBox
        );

    }

    private void swapFav() {
        isFav = !isFav;
        updateFavIcon();
    }

    private void updateFavIcon() {
        if ( isFav ) {
            favIcon.setIconLiteral("ci-star-filled");
        } else {
            favIcon.setIconLiteral("ci-star");
        }
    }

    public VBox getContent() { return this.vb; }
    public int getID() { return this.recipeID; }
}