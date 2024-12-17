module com.example.k105 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports com.example.k105.controllers;
    exports com.example.k105.dao;
    exports com.example.k105.models;

    opens com.example.k105.models to javafx.fxml; // Открываем для FXMLLoader
}
