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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RecipeShowingController implements Initializable {

    private long recipeId;

    //JAVAFX
    @FXML private Label lRecipeTitle;
    @FXML private WebView wbRecipeContent;
    @FXML private WebEngine webEngine;
    @FXML private FontIcon fiFavourite;
    @FXML private Label lNumberOfFavs;

    private boolean favourite;

    // METHODS
    @FXML
    private void goBack() {
        try {
            App.goBack();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void toggleFavouriteRecipe() {
        // TODO add to favourite list from server
        favourite = !favourite;

        // ICON SWAP AND LOCAL INCREMENT
        if (favourite) { // favourite
            fiFavourite.setIconLiteral("ci-star-filled");
            int i = Integer.parseInt( lNumberOfFavs.getText() );
            i++;
            lNumberOfFavs.setText( Integer.toString(i) );
        } else { //unfavourite
            fiFavourite.setIconLiteral("ci-star");
            int i = Integer.parseInt( lNumberOfFavs.getText() );
            i--;
            lNumberOfFavs.setText( Integer.toString(i) );

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = wbRecipeContent.getEngine();

        // DEFAULTS
        lRecipeTitle.setText(resourceBundle.getString("label.base.recipe.name"));
        webEngine.loadContent("Content could not be loaded. If you are seeing this message, is probably because the programmers did something wrong.", "text/plain");
        favourite = false;
        fiFavourite.setIconLiteral("ci-star");
        lNumberOfFavs.setText("0");

        // REST API call
        WebTarget target = ServerConnection.getInstance().getTarget("recipes/id/" + recipeId);
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            RecipeData data = response.readEntity(RecipeData.class);

            lRecipeTitle.setText(data.getTitle());
            String html = toMarkdown(data.getContent());
            webEngine.loadContent(html, "text/html");

        }

        // TODO get number of likes and if it's been added to favorites or not

    }

    public void setRecipeId(long recipeId) {

        this.recipeId = recipeId;
    }

    public String toMarkdown(String input) {

        Parser parser = Parser.builder().build();
        Node document = parser.parse(input);

        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
