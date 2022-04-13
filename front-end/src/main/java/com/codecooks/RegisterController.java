package com.codecooks;

import com.codecooks.serialize.RegistrationData;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField tEmail;
    @FXML private TextField tUsername;
    @FXML private PasswordField passField;


    @FXML
    public void register() {

        RegistrationData data = new RegistrationData();
        data.setEmail(tEmail.getText());
        data.setUsername(tUsername.getText());
        data.setPassword(org.apache.commons.codec.digest.DigestUtils.sha1Hex(passField.getText()));

        WebTarget target = ServerConnection.getInstance().getTarget("/account");
        Response response = target.request().post(Entity.entity(data, MediaType.APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            toLogin();
        }

        else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Register error");
            alert.setContentText("Invalid register!");
            App.showAlertAndWait(alert);

        }
    }

    @FXML
    public void toLogin() {

        try {
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}