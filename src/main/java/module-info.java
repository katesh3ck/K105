module com.example.course {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    //requires org.slf4j;

    exports com.example.course;
    exports com.example.course.controllers;

    opens com.example.course.models to javafx.base;
    opens com.example.course.controllers to javafx.fxml;
}
