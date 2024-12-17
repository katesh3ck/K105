package com.example.k105;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Загружаем FXML-файл
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/k105/main.fxml"));
        Parent root = loader.load();

        // Устанавливаем сцену
        primaryStage.setTitle("Система расчёта премий");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
