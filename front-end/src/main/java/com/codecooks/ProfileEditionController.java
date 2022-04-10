package com.codecooks;

import com.codecooks.domain.CookingExperience;
import com.codecooks.domain.Gender;
import com.codecooks.serialize.ProfileEditionData;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ProfileEditionController implements Initializable {

    private ObservableList<CookingExperience> cookingExpOptions =
            FXCollections.observableArrayList(CookingExperience.values());
    private ObservableList<Gender> genderOptions =
            FXCollections.observableArrayList(Gender.values());
    private ObservableList<Country> countryOptions =
            FXCollections.observableArrayList(Country.Bilbao, Country.Turkey, Country.Spain);

    @FXML private ChoiceBox<CookingExperience> cbCookingExp;
    @FXML private ChoiceBox<Gender> cbGender;
    @FXML private ChoiceBox<Country> cbCountry;
    @FXML private DatePicker dpBirthDate;
    @FXML private ImageView ivUserAvatar;
    @FXML private Label lUsername;
    @FXML private TextField tfName;
    @FXML private Hyperlink hlChangePicture;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        cbCookingExp.setItems(cookingExpOptions);
        cbGender.setItems(genderOptions);
        cbCountry.setItems(countryOptions);

        hlChangePicture.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg")
            );

            File selectedFile = fileChooser.showOpenDialog(null); // TODO Show relative to main stage (the same with alerts)
            if (selectedFile != null) {

                ivUserAvatar.setImage(new Image(selectedFile.toURI().toString()));
            }
        });

        WebTarget target = ServerConnection.getInstance().getTarget("profile/edit");
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            ProfileEditionData data = response.readEntity(ProfileEditionData.class);

            if (data.getName() != null) tfName.setText(data.getName());
            if (data.getBirthDate() != null) dpBirthDate.setValue(data.getBirthDate());
            if (data.getGender() != null) cbGender.setValue(data.getGender());
            if (data.getCookingExp() != null) cbCookingExp.setValue(data.getCookingExp());

        }
    }

    @FXML
    private void goBack() throws IOException {
            App.goBack();
    }

    @FXML
    private void saveChanges() {

        String name = tfName.getText();
        LocalDate birthDate = dpBirthDate.getValue();
        CookingExperience cookingExp = cbCookingExp.getValue();
        Gender gender = cbGender.getValue();

        ProfileEditionData data = new ProfileEditionData();
        data.setName(name);
        data.setBirthDate(birthDate);
        data.setGender(gender);
        data.setCookingExp(cookingExp);

        WebTarget target = ServerConnection.getInstance().getTarget("profile/edit");
        Response response = target.request().post(Entity.entity(data, MediaType.APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            try {
                App.goBack();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

// TODO Include all countries and their flags
enum Country {
    Bilbao("Bilbao"),
    Turkey("Turkey"),
    Spain("Spain");

    private final String country;

    Country(String c) { country = c; }
    public String toString() { return country; }
}

