package com.example.k105.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import com.example.k105.dao.EmployeeDAO;


public class MainController {

    private EmployeeDAO employeeDAO = new EmployeeDAO();

    @FXML private Button calculateBonusButton; // Кнопка для расчёта премий
    @FXML private TextArea resultTextArea;     // Поле для вывода результатов
    @FXML private Label statusLabel;           // Статус операции

    @FXML
    public void initialize() {
        // Обработчик нажатия кнопки "Рассчитать премии"
        calculateBonusButton.setOnAction(e -> {
            try {
                employeeDAO.calculateAndInsertBonuses(); // Вызываем метод из DAO
                statusLabel.setText("Премии успешно рассчитаны и добавлены в базу данных.");
                resultTextArea.setText("Премии успешно рассчитаны!"); // Вывод сообщения
            } catch (Exception ex) {
                ex.printStackTrace();
                statusLabel.setText("Ошибка при расчёте премий!");
                resultTextArea.setText("Произошла ошибка: " + ex.getMessage());
            }
        });

    }
}
