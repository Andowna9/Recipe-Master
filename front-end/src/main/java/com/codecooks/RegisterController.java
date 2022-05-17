package com.codecooks;

import com.codecooks.serialize.RegistrationData;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import net.synedra.validatorfx.Validator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML private TextField tEmail;
    @FXML private TextField tUsername;
    @FXML private PasswordField passField;

    private String invalidRegister;
    private Validator validator = new Validator();

    @FXML
    public void register() {

        if (validator.validate()) {

            RegistrationData data = new RegistrationData();
            data.setEmail(tEmail.getText());
            data.setUsername(tUsername.getText());
            data.setPassword(org.apache.commons.codec.digest.DigestUtils.sha1Hex(passField.getText()));

            WebTarget target = ServerConnection.getInstance().getTarget("/account");
            Response response = target.request().post(Entity.entity(data, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                toLogin();

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(invalidRegister);
                App.showAlertAndWait(alert);

            }
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        invalidRegister = resourceBundle.getString("alert.invalid.register");

        validator.createCheck()
                .withMethod(context -> {
                    String email = context.get("email");
                    if (email.isEmpty() || email.isBlank()) {
                        context.error("Email is required!");
                    }

                    else if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                        context.error("Incorrect format!");
                    }
                })
                .dependsOn("email", tEmail.textProperty())
                .decoratingWith(ValidatorDecorations::RedBorderDecoration)
                .decorates(tEmail);

        validator.createCheck()
                .withMethod(context -> {
                    String email = context.get("username");
                    if (email.isEmpty() || email.isBlank()) {
                        context.error("Username is required!");
                    }
                })
                .dependsOn("username", tUsername.textProperty())
                .decoratingWith(ValidatorDecorations::RedBorderDecoration)
                .decorates(tUsername);

        validator.createCheck()
                .withMethod(context -> {
                    String email = context.get("password");
                    if (email.isEmpty() || email.isBlank()) {
                        context.error("Password is required!");
                    }
                })
                .dependsOn("password", passField.textProperty())
                .decoratingWith(ValidatorDecorations::RedBorderDecoration)
                .decorates(passField);

    }
}