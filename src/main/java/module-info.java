module com.example.k105 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example.k105 to javafx.fxml;
    exports com.example.k105;
    exports com.example.k105.controllers;
    opens com.example.k105.controllers to javafx.fxml;
    exports com.example.k105.dao;
    opens com.example.k105.dao to javafx.fxml;
}