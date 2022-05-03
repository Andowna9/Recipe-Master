package com.codecooks.utils;

import java.util.ResourceBundle;

public class I18n {

    private I18n() {}

    private static final String BASE_PATH = "i18n/";
    private static ResourceBundle enumsBundle;

    public static String getEnumTranslation(Enum<?> value) {

        if (enumsBundle == null) {
            enumsBundle = ResourceBundle.getBundle(BASE_PATH + "enums");
        }

        return enumsBundle.getString("enum." + value.toString());
    }

    public static ResourceBundle getFXMLBundle(String fxml) {
        return ResourceBundle.getBundle(BASE_PATH + fxml);
    }
}
