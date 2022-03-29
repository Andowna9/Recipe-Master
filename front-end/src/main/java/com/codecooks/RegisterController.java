package com.codecooks;

import javafx.fxml.FXML;

import java.io.IOException;

public class RegisterController {

    @FXML
    public void register() {

        // TODO
    }

    @FXML
    public void cancel() {

        try {
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}