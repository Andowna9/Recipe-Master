package com.codecooks;

import com.codecooks.serialize.RecipeData;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RecipeShowingController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(RecipeShowingController.class);

    private long recipeId;

    //JAVAFX
    @FXML private Label lRecipeTitle;
    @FXML private Label authorUName;
    @FXML private WebView wbRecipeContent;
    @FXML private Label recipeCountry;
    @FXML private WebEngine webEngine;
    @FXML private FontIcon fiFavourite;
    @FXML private Label lNumberOfFavs;

    private WebTarget target;
    private boolean isFavourite;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        target = ServerConnection.getInstance().getTarget("recipes/" + recipeId);
        webEngine = wbRecipeContent.getEngine();

        // DEFAULTS
        lRecipeTitle.setText(resourceBundle.getString("label.base.recipe.name"));
        webEngine.loadContent("Content could not be loaded. If you are seeing this message, is probably because the programmers did something wrong.", "text/plain");
        isFavourite = false;
        fiFavourite.setIconLiteral("ci-star");
        lNumberOfFavs.setText("0");

        // REST API call
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            RecipeData data = response.readEntity(RecipeData.class);


            authorUName.setText(data.getAuthorUsername());
            recipeCountry.setText(data.getCountryCode());

            lRecipeTitle.setText(data.getTitle());
            String html = toMarkdown(data.getContent());
            webEngine.loadContent(html, "text/html");

            isFavourite = data.isFavourite();
            fiFavourite.setIconLiteral(isFavourite? "ci-star-filled": "ci-star");

            lNumberOfFavs.setText(String.valueOf(data.getNumFavourites()));

        }

    }

    // METHODS
    @FXML
    private void goBack() {
        try {
            App.goBack();
        } catch (IOException e) {
            logger.error("Error going back", e);
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleFavouriteRecipe() {

        WebTarget favTarget = target.path("favourite");
        Response response;

        if (isFavourite) {
            response = favTarget.request().delete();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                fiFavourite.setIconLiteral("ci-star");
                isFavourite = false;
            }

            int i = Integer.parseInt( lNumberOfFavs.getText() );
            i--;
            lNumberOfFavs.setText( Integer.toString(i) );

        } else {
            response = favTarget.request().get();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                fiFavourite.setIconLiteral("ci-star-filled");
                isFavourite = true;
            }

            int i = Integer.parseInt( lNumberOfFavs.getText() );
            i++;
            lNumberOfFavs.setText( Integer.toString(i) );

        }

    }

    public String toMarkdown(String input) {

        Parser parser = Parser.builder().build();
        Node document = parser.parse(input);

        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    public void setRecipeId(long recipeId) {

        this.recipeId = recipeId;
    }
}
