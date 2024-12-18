package com.example.k105.models;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Загрузка основного FXML-файла
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/k105/main.fxml"));
        Parent root = loader.load();

        // Установка сцены
        primaryStage.setTitle("Система расчета премий сотрудников");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
