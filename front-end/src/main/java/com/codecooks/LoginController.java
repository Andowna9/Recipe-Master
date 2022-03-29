package com.codecooks;

import jakarta.ws.rs.client.WebTarget;
import javafx.fxml.FXML;

import java.io.IOException;

public class LoginController {

    @FXML
    private void login() {

        // TODO
    }

    @FXML
    private void toRegister() {

        try {
            App.setRoot("register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
