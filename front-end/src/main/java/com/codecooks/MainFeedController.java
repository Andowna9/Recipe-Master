package com.codecooks;

import com.codecooks.serialize.RecipeBriefData;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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
        int recipeDisplayLimit = 10;
        int recipeWidth = 175; int separation = 10; int rightMargin = 20;

        // Popular recipes
        WebTarget popularTarget = ServerConnection.getInstance().getTarget("recipes/popular");
        Response popularResponse = popularTarget.request(MediaType.APPLICATION_JSON).get();

        if (popularResponse.getStatus() == Response.Status.OK.getStatusCode()) {

            List<RecipeBriefData> briefs = popularResponse.readEntity(new GenericType<List<RecipeBriefData>>() {});
            amtOfPopRecipes = briefs.size();

            if (amtOfPopRecipes > 0) {
                for (int i = 0; i < amtOfPopRecipes && i < recipeDisplayLimit; i++) {

                    RecipeBriefData brief = briefs.get(i);
                    VBox vb = new FeedRecipeContainer(brief.getId(), brief.getTitle()).getContent();
                    hbPopRecipes.getChildren().add(vb);

                }

                hbPopRecipes.setPrefWidth( amtOfPopRecipes*recipeWidth + amtOfPopRecipes*separation + rightMargin);
                apPopRecipes.setPrefWidth( hbPopRecipes.getPrefWidth() );
            }

            else {
                hbPopRecipes.getChildren().add( new Label("No recipes found") );
            }
        }


        // Recent recipes
        WebTarget recentTarget = ServerConnection.getInstance().getTarget("recipes/recent");
        Response recentResponse = recentTarget.request(MediaType.APPLICATION_JSON).get();

        if (recentResponse.getStatus() == Response.Status.OK.getStatusCode()) {

            List<RecipeBriefData> briefs = recentResponse.readEntity(new GenericType<List<RecipeBriefData>>() {});
            amtOfRecentRecipes = briefs.size();

            if (amtOfRecentRecipes > 0) {
                for (int i = 0; i < amtOfRecentRecipes && i < recipeDisplayLimit; i++) {

                    RecipeBriefData brief = briefs.get(i);
                    VBox vb = new FeedRecipeContainer(brief.getId(), brief.getTitle()).getContent();
                    hbRecentRecipes.getChildren().add(vb);

                }

                hbRecentRecipes.setPrefWidth( amtOfRecentRecipes*recipeWidth + amtOfRecentRecipes*separation + rightMargin);
                apRecentRecipes.setPrefWidth( hbRecentRecipes.getPrefWidth() );

            } else {
                hbRecentRecipes.getChildren().add( new Label("No recipes found") );
            }

        }

    }
}

class FeedRecipeContainer {

    private long recipeID;
    private String recipeTitle;
    private boolean isFav;
    @FXML private VBox vb;
    @FXML private FontIcon favIcon;

    FeedRecipeContainer(long id, String title) {
        this.recipeID = id;
        this.recipeTitle = title;

        // INLINE CSS
        vb = new VBox();
        vb.setStyle("-fx-border-style: solid; -fx-border-color: grey; -fx-background-color: #eaeaea");
        vb.setPadding(new Insets(5,5,5,5));
        vb.setSpacing(5); vb.setPrefWidth(175); vb.setPrefHeight(85);

        // LABELS
        Label lTitle = new Label(recipeTitle);
        lTitle.setStyle("-fx-font-size: 16");
        Label lAuthor = new Label("Author");

        // BUTTON MENU (BUTTONS AND ICONS)
        HBox buttonBox = new HBox();
        Button bOpen= new Button(); Button bFav = new Button();

        // tooltips
        bOpen.setTooltip( new Tooltip("Show recipe") );
        bFav.setTooltip( new Tooltip("Add to favs") );
        //icons
        FontIcon viewIcon = new FontIcon(); viewIcon.setIconLiteral("ci-view"); viewIcon.setIconSize(16);
        favIcon= new FontIcon(); favIcon.setIconSize(16);
        isFav = false; // TODO get if it was already favorited
        updateFavIcon();

        // misc
        bOpen.setGraphic(viewIcon); bFav.setGraphic(favIcon);
        buttonBox.setSpacing(5);
        buttonBox.getChildren().addAll(bOpen, bFav);

        // BUTTON LISTENERS
        bOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    RecipeShowingController controller = new RecipeShowingController();
                    controller.setRecipeId(getID());
                    App.setRoot("recipeShow", controller);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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
    public long getID() { return this.recipeID; }
}