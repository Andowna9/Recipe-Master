module com.codecooks {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.codecooks to javafx.fxml;
    exports com.codecooks;
}
