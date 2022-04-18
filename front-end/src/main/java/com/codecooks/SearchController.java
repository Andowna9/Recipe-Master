package com.codecooks;

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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

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

    @FXML
    private void updateSearch() {
        //piLoading.setVisible(true); // To show to the user that the search is being done. Run in another Thread?
        String searchTerm = tfSearchItem.getText();
        String searchType = ( (RadioButton) tgSearchType.getSelectedToggle() ).getText();

        System.out.println("Searching for " + searchTerm + " among " + searchType + "s");
    }

    @FXML
    private void reverseSort() {
        descendingOrder = !descendingOrder;

        if(descendingOrder) { fiDescAsc.setIconLiteral("ci-arrow-down"); lDescAsc.setText("DESC"); }
        else { fiDescAsc.setIconLiteral("ci-arrow-up"); lDescAsc.setText("ASC"); }

    }

    protected void displayResultItem() throws IOException {
        // TODO swap to the other view. Differentiate user from recipe?
    }

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
        rbRecipeSearch.setToggleGroup( tgSearchType );
        rbUserSearch.setToggleGroup( tgSearchType );
        tgSearchOrder = new ToggleGroup();
        rbPopularitySort.setToggleGroup( tgSearchOrder );
        rbDateSort.setToggleGroup( tgSearchOrder );

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


        // LIST OF ITEMS
        // If there are no recipes posted
        if (searchItemObservableList.isEmpty()) {

            lvSearchResults.setDisable(true);

            HBox hb = new HBox();
            Label lb = new Label("No recipes found");
            lb.setFont(Font.font(22));
            hb.getChildren().add(lb);

            hb.setAlignment( Pos.CENTER );
            AnchorPane.setLeftAnchor(hb, 0.0);
            AnchorPane.setRightAnchor(hb, 0.0);
            AnchorPane.setTopAnchor(hb, 0.0);
            AnchorPane.setBottomAnchor(hb, 0.0);

            resultsPane.getChildren().add(hb);

        }

        else {

            lvSearchResults.setItems(searchItemObservableList);
            lvSearchResults.setCellFactory( lvSearchResults -> new ResultListViewCell(this));
            lvSearchResults.setMouseTransparent(false);
            lvSearchResults.setFocusTraversable(false);
        }
    }
}

// SUPPORTING CLASSES FOR THE LIST DISPLAY
class ResultListViewCell extends ListCell<SearchResultItemData> {

    @FXML private AnchorPane apItemContainer;
    @FXML private Label lResultName;
    @FXML private Button bShowItem;

    private SearchController searchController;
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
                e.printStackTrace();
            }

            // TODO get item details
            lResultName.setText( res.getName() );

            // TODO change view (method up in the controller)
            bShowItem.setOnAction( actionEvent -> {
                try {
                    searchController.displayResultItem();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            setText(null);
            setGraphic(apItemContainer);
        }
    }
}

class SearchResultItemData {
    private String name;

    public String getName() { return name; }
    public SearchResultItemData(String name) { this.name = name; }
}