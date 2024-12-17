module com.example.k105 {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;

    exports com.example.k105.controllers;
    exports com.example.k105.dao; // Экспорт пакета DAO

}
