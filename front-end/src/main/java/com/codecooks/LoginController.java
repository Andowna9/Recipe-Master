package com.codecooks;

import jakarta.ws.rs.client.WebTarget;
import javafx.fxml.FXML;

public class LoginController {
   private WebTarget target = ServerConnection.getInstance().getTarget().path("user/login");

    @FXML
    private void login() {
    }

}
