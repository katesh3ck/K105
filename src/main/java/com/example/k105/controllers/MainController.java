package com.example.k105.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    @FXML
    private TableView<Employee> tableView;

    @FXML
    private TableColumn<Employee, Integer> idColumn;

    @FXML
    private TableColumn<Employee, String> nameColumn;

    @FXML
    private TableColumn<Employee, String> positionColumn;

    @FXML
    private Button calculateBonusButton;

    private final ObservableList<Employee> employeeData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Привязываем колонки к данным
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        // Добавляем тестовые данные
        employeeData.addAll(
                new Employee(1, "Анна Иванова", "Менеджер"),
                new Employee(2, "Иван Петров", "Разработчик"),
                new Employee(3, "Мария Смирнова", "Дизайнер")
        );

        // Добавляем данные в таблицу
        tableView.setItems(employeeData);

        // Логика кнопки
        calculateBonusButton.setOnAction(event -> System.out.println("Бонусы рассчитаны!"));
    }

    // Вспомогательный класс Employee
    public static class Employee {
        private final Integer id;
        private final String name;
        private final String position;

        public Employee(Integer id, String name, String position) {
            this.id = id;
            this.name = name;
            this.position = position;
        }

        public Integer getId() { return id; }
        public String getName() { return name; }
        public String getPosition() { return position; }
    }
}
