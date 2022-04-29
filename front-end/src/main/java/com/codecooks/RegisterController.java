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
import org.controlsfx.control.decoration.Decorator;
import org.controlsfx.control.decoration.StyleClassDecoration;
import org.controlsfx.validation.Severity;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javax.print.DocFlavor;
import java.io.IOException;
import java.util.ResourceBundle;

import static java.lang.Boolean.TRUE;

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

    public void initialize(DocFlavor.URL url, ResourceBundle resourceBundle) {
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(tEmail, Validator.createEmptyValidator("Email is required!"));
        if (validationSupport.registerValidator(tEmail, Validator.createRegexValidator(String.valueOf(tEmail),
                "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*" +
                        "(\\\\.[A-Za-z]{2,})$", Severity.ERROR)) == TRUE){
            Decorator.addDecoration(tEmail, new StyleClassDecoration("warning")); //TODO

        }
        validationSupport.registerValidator(passField, Validator.createEmptyValidator("Password is required!"));
        validationSupport.registerValidator(tUsername, Validator.createEmptyValidator("Username is required!"));

        /** REGEX CONDITIONS
         The following restrictions are imposed in the email addresses local-part by using this regex:
         - It allows numeric values from 0 to 9
         - Both uppercase and lowercase letters from a to z are allowed
         - Allowed are underscore “_”, hyphen “-” and dot “.”Dot isn't allowed at the start and end of the local-part
         - Consecutive dots aren't allowed
         - For the local part, a maximum of 64 characters are allowed

         Restrictions for the domain-part in this regular expression include
         - It allows numeric values from 0 to 9
         - We allow both uppercase and lowercase letters from a to z
         - Hyphen “-” and dot “.” isn't allowed at the start and end of the domain-part
         - No consecutive dots
         */
    }
}