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

import net.synedra.validatorfx.Validator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @FXML private TextField tfEmail;
    @FXML private PasswordField passField;
    @FXML private ToggleSwitch tglRememberMe;

    private Validator validator = new Validator();

    @FXML
    private void login() {

        if (validator.validate()) {

            Credentials credentials = new Credentials();
            credentials.setEmail(tfEmail.getText());
            credentials.setPassword(org.apache.commons.codec.digest.DigestUtils.sha1Hex(passField.getText()));

            WebTarget target = ServerConnection.getInstance().getTarget("/account/login");
            Response response = target.request(MediaType.TEXT_PLAIN).post(Entity.entity(credentials, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {

                String token = response.readEntity(String.class);
                ServerConnection.getInstance().setAuthToken(token);

                logger.info("Token: {}", token);


                if (tglRememberMe.isSelected()) {
                    AppConfiguration.setConfig("email", tfEmail.getText());

                } else {
                    AppConfiguration.removeConfig("email");
                }

                try {
                    MainController controller = new MainController();
                    App.setRoot("main", controller);
                } catch (IOException e) {
                    logger.error("Error validating", e);
                    e.printStackTrace();
                }

            } else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid credentials!");
                App.showAlertAndWait(alert);
            }
        }
    }

    @FXML
    private void toRegister() {

        try {
            App.setRoot("register");
        } catch (IOException e) {
            logger.error("Failed to register", e);
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        validator.createCheck()
                .withMethod(context -> {
                    String email = context.get("email");
                    if (email.isEmpty() || email.isBlank()) {
                        context.error("Email is required!");
                    }
                })
                .dependsOn("email", tfEmail.textProperty())
                .decoratingWith(ValidatorDecorations::RedBorderDecoration)
                .decorates(tfEmail);

        validator.createCheck()
                .withMethod(context -> {
                    String  password = context.get("password");
                    if (password.isEmpty() | password.isBlank()) {
                        context.error("Password is required!");
                    }
                })
                .dependsOn("password", passField.textProperty())
                .decoratingWith(ValidatorDecorations::RedBorderDecoration)
                .decorates(passField);

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
