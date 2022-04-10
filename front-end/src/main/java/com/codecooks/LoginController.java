package com.codecooks;

import com.codecooks.serialize.Credentials;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.controlsfx.control.ToggleSwitch;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField tfEmail;
    @FXML private PasswordField passField;
    @FXML private ToggleSwitch tglRememberMe;


    @FXML
    private void login() {

        Credentials credentials = new Credentials();
        credentials.setEmail(tfEmail.getText());
        credentials.setPassword(org.apache.commons.codec.digest.DigestUtils.sha1Hex(passField.getText()));

        WebTarget target = ServerConnection.getInstance().getTarget("/account/login");
        Response response = target.request(MediaType.TEXT_PLAIN).post(Entity.entity(credentials, MediaType.APPLICATION_JSON));

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            String token = response.readEntity(String.class);
            ServerConnection.getInstance().setAuthToken(token);
            System.out.println("Token: " + token);


            if (tglRememberMe.isSelected()) {
                AppConfiguration.setConfig("email", tfEmail.getText());

            } else {
                AppConfiguration.removeConfig("email");
            }

            try {
                App.setRoot("main");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Invalid credentials!");
            alert.showAndWait();
        }
    }

    @FXML
    private void toRegister() {

        try {
            App.setRoot("register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String storedEmail = AppConfiguration.getConfig("email", "");

        if (!storedEmail.isEmpty()) {
            tfEmail.setText(storedEmail);
            tglRememberMe.setSelected(true);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    passField.requestFocus();
                }
            });

        }


    }
}
