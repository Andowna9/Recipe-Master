package com.codecooks;

import javafx.fxml.FXML;

import java.io.IOException;

public class RecipePostingController {

    @FXML
    private void postContent() {

    }

    @FXML
    private void goBack() {
        try {
            App.goBack();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
