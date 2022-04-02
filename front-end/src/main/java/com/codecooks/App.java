package com.codecooks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Parent previousFXML;

    @Override
    public void start(Stage stage) throws IOException {

        double height = 400.0;
        double width  = 600.0;
        scene = new Scene(loadFXML("main"), width, height);
        stage.setScene(scene);
        stage.setTitle("Recipe Master");
        stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("img/food-icon-cc-64.png"))));
        stage.sizeToScene();
        stage.show();
        stage.setMinWidth(width);
        stage.setMinHeight(height);
    }

    static void setRoot(String fxml) throws IOException {
        previousFXML = scene.getRoot();
        scene.setRoot(loadFXML(fxml));
    }

    static void goBack() throws IOException {
        scene.setRoot(previousFXML);
    }

    protected static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}