package com.codecooks;

import javafx.scene.Node;
import net.synedra.validatorfx.Decoration;
import net.synedra.validatorfx.ValidationMessage;

public class ValidatorDecorations {

    public static Decoration RedBorderDecoration(ValidationMessage m) {

        return new Decoration() {
            @Override
            public void add(Node node) {
                node.setStyle("-fx-border-color: red; -fx-focus-color: red; -fx-border-radius: 3px;");
            }

            @Override
            public void remove(Node node) {
                node.setStyle("");
            }
        };
    }
}
