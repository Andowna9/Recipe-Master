package com.codecooks;

import com.codecooks.serialize.RecipeBriefData;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import org.controlsfx.control.HiddenSidesPane;
import org.kordamp.ikonli.javafx.FontIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private boolean descendingOrder;
    @FXML private StackPane resultsPane;
    @FXML private ListView<SearchResultItemData> lvSearchResults;
    private static ObservableList<SearchResultItemData> searchItemObservableList;

    @FXML private TextField tfSearchItem;
    @FXML private ProgressIndicator piLoading;

    @FXML private HiddenSidesPane sidePane;
    @FXML private FontIcon cbPinned;
    @FXML private FontIcon fiDescAsc;
    @FXML private Label lDescAsc;

    @FXML private RadioButton rbRecipeSearch;
    @FXML private RadioButton rbUserSearch;
    @FXML private RadioButton rbPopularitySort;
    @FXML private RadioButton rbDateSort;
    private ToggleGroup tgSearchType;
    private ToggleGroup tgSearchOrder;

    private HBox noContentPane;
    private Label lblNoContent;
    private String searchType;

    public SearchController() {
        searchItemObservableList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Loading icon false by default
        piLoading.setVisible(false);

        // Order
        descendingOrder = true;

        // DIFFERENT TYPES OF SEARCHES and SORTS
        tgSearchType = new ToggleGroup();
        rbRecipeSearch.setToggleGroup(tgSearchType);
        rbUserSearch.setToggleGroup(tgSearchType);
        tgSearchOrder = new ToggleGroup();
        rbPopularitySort.setToggleGroup(tgSearchOrder);
        rbDateSort.setToggleGroup(tgSearchOrder);

        // Default radio button
        rbRecipeSearch.setSelected(true);
        rbUserSearch.setSelected(false);
        rbPopularitySort.setSelected(true);
        rbDateSort.setSelected(false);

        // To prevent the user from unselecting a radio button (one is required)
        tgSearchType.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });
        tgSearchOrder.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });


        // Pane when no search results were found
        noContentPane = new HBox();
        lblNoContent = new Label("No results found");
        lblNoContent.setFont(Font.font(22));
        noContentPane.getChildren().add(lblNoContent);

        noContentPane.setAlignment(Pos.CENTER);
        AnchorPane.setLeftAnchor(noContentPane, 0.0);
        AnchorPane.setRightAnchor(noContentPane, 0.0);
        AnchorPane.setTopAnchor(noContentPane, 0.0);
        AnchorPane.setBottomAnchor(noContentPane, 0.0);

        // Setting up ListView
        lvSearchResults.setItems(searchItemObservableList);
        lvSearchResults.setCellFactory(lvSearchResults -> new ResultListViewCell(this));
        lvSearchResults.setMouseTransparent(false);
        lvSearchResults.setFocusTraversable(false);

        resultsPane.getChildren().add(noContentPane);

        reloadSearchList();
    }


    @FXML
    private void pinSidePane() {
        if (sidePane.getPinnedSide() != null) {
            sidePane.setPinnedSide(null);
            cbPinned.setIconLiteral("ci-pin");
        } else {
            sidePane.setPinnedSide(Side.LEFT);
            cbPinned.setIconLiteral("ci-pin-filled");
        }
    }

    public void reloadSearchList() {

        // If there are no recipes posted
        if (searchItemObservableList.isEmpty()) {

            lvSearchResults.setDisable(true);
            noContentPane.setVisible(true);

        } else {

            lvSearchResults.setDisable(false);
            noContentPane.setVisible(false);
        }

    }

    private void searchUser(String searchTerm) {

        WebTarget target = ServerConnection.getInstance().getTarget("/users");
        Response response = target.queryParam("username", searchTerm).request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            List<String> usernames = response.readEntity(new GenericType<List<String>>() {});
            for (String username: usernames) {

                SearchResultItemData itemData = new SearchResultItemData(username, -1);
                searchItemObservableList.add(itemData);

            }
        }

    }

    private void searchRecipe(String searchTerm) {


        WebTarget target = ServerConnection.getInstance().getTarget("/recipes");
        Response response = target.queryParam("title", searchTerm).request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            List<RecipeBriefData> recipes = response.readEntity(new GenericType<List<RecipeBriefData>>() {});
            for (RecipeBriefData recipe : recipes) {

                SearchResultItemData itemData = new SearchResultItemData(recipe.getTitle(), recipe.getId());
                searchItemObservableList.add(itemData);
            }
        }

    }


    @FXML
    private void updateSearch() {

        //piLoading.setVisible(true); // To show to the user that the search is being done. Run in another Thread?
        searchItemObservableList.clear();

        String searchTerm = tfSearchItem.getText();
        searchType = ((RadioButton) tgSearchType.getSelectedToggle()).getText();

        logger.info("Searching for {} among {}s", searchTerm, searchType);

        if (searchType.equals("Recipe")) {

            searchRecipe(searchTerm);
        }

        else {

            searchUser(searchTerm);
        }


        reloadSearchList();

    }

    @FXML
    private void reverseSort() {
        descendingOrder = !descendingOrder;

        if (descendingOrder) {
            fiDescAsc.setIconLiteral("ci-arrow-down");
            lDescAsc.setText("DESC");
        } else {
            fiDescAsc.setIconLiteral("ci-arrow-up");
            lDescAsc.setText("ASC");
        }

    }

    protected void displayResultItem(SearchResultItemData item) {

        // TODO Differentiate user from recipe?

        try {

            if (searchType.equals("Recipe")) {

                RecipeShowingController controller = new RecipeShowingController();
                controller.setRecipeId(item.getId());
                App.setRoot("recipeShow", controller);

            }

            else {

                // TODO
                logger.warn("TODO"); //No habia un syso antes aqui
            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error displaying result item", e); //No habia un syso antes aqui
        }


    }
}


// SUPPORTING CLASSES FOR THE LIST DISPLAY
class ResultListViewCell extends ListCell<SearchResultItemData> {

    private static final Logger logger = LoggerFactory.getLogger(ResultListViewCell.class);

    @FXML private AnchorPane apItemContainer;
    @FXML private Label lResultName;
    @FXML private Button bShowItem;

    private final SearchController searchController;
    public ResultListViewCell(SearchController sc) {
        this.searchController = sc;
    }

    @Override
    protected void updateItem(SearchResultItemData res, boolean empty) {
        super.updateItem(res, empty);

        if (empty || res == null) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader fml = new FXMLLoader(App.class.getResource( "searchItemContainer.fxml"));
            fml.setController(this);

            try{
                fml.load();
            } catch (IOException e) {
                logger.error("Error searching the item", e);
                e.printStackTrace();
            }

            lResultName.setText( res.getName() );

            bShowItem.setOnAction( actionEvent -> {
                    searchController.displayResultItem(res);
            });

            setText(null);
            setGraphic(apItemContainer);
        }
    }
}

class SearchResultItemData {
    private long id;
    private String name;

    public SearchResultItemData(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }
    public long getId() { return id; }
}