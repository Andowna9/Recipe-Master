package com.codecooks;

import com.codecooks.serialize.RecipeData;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class RecipeShowingController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(RecipeShowingController.class);

    public enum FromView {
        HOME, PROFILE, SEARCH
    }

    private FromView fromView;
    private long recipeId;

    //JAVAFX
    @FXML private Label lRecipeTitle;
    @FXML private Label authorUName;
    @FXML private ImageView ivAvatarAuthor;
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
        ivAvatarAuthor.setPreserveRatio(false);

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

            WebTarget avatarTarget = ServerConnection.getInstance().getTarget("users/" + data.getAuthorUsername() + "/avatar");
            response = avatarTarget.request("image/png", "image/jpg").get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {

                InputStream is = response.readEntity(InputStream.class);
                ivAvatarAuthor.setImage(new Image(is));

            }

        }

    }

    // METHODS
    @FXML
    private void goBack() {

        if (fromView == FromView.HOME) {
            try {
                MainController controller = new MainController();
                controller.setSubView(MainController.SubView.HOME);
                App.setRoot("main", controller);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if (fromView == FromView.PROFILE) {

            try {
                MainController controller = new MainController();
                controller.setSubView(MainController.SubView.PROFILE);
                App.setRoot("main", controller);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else if (fromView == FromView.SEARCH) {

            try {
                App.goBack();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

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

    public void setFromView(FromView fromView) {

        this.fromView = fromView;
    }
}
