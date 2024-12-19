package com.example.course.controllers;

import com.example.course.models.Employee;
import com.example.course.dao.EmployeeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class HelloController {

    @FXML private TableView<Employee> tableView;
    @FXML private TableColumn<Employee, Integer> idColumn;
    @FXML private TableColumn<Employee, String> nameColumn;
    @FXML private TableColumn<Employee, String> departmentColumn;
    @FXML private TableColumn<Employee, Double> salaryColumn;
    @FXML private TableColumn<Employee, LocalDate> hireDateColumn;
    @FXML private TableColumn<Employee, Double> bonusColumn;
    @FXML private Button loadButton;
    @FXML private Button calculateButton;

    private final ObservableList<Employee> employeeData = FXCollections.observableArrayList();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hireDate"));

        // Настройка отображения зарплаты с запятой
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        salaryColumn.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            private final DecimalFormat format = new DecimalFormat("#,##0.00");

            @Override
            protected void updateItem(Double salary, boolean empty) {
                super.updateItem(salary, empty);
                if (empty || salary == null) {
                    setText(null);
                } else {
                    setText(format.format(salary)); // Используем DecimalFormat для отображения с запятой
                }
            }
        });

        // Настройка отображения премий с двумя знаками после запятой
        bonusColumn.setCellValueFactory(new PropertyValueFactory<>("bonus"));
        bonusColumn.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            private final DecimalFormat format = new DecimalFormat("#,##0.00");

            @Override
            protected void updateItem(Double bonus, boolean empty) {
                super.updateItem(bonus, empty);
                if (empty || bonus == null) {
                    setText(null);
                } else {
                    setText(format.format(bonus)); // Отображение премий в формате с запятой
                }
            }
        });

        loadButton.setOnAction(event -> loadDataFromDatabase());
        calculateButton.setOnAction(event -> calculateBonuses());
    }


    /**
     * Загрузка данных сотрудников из базы данных.
     */
    private void loadDataFromDatabase() {
        employeeData.clear();
        employeeData.addAll(employeeDAO.getAllEmployees());
        tableView.setItems(employeeData);
    }

    /**
     * Расчёт и сохранение премий сотрудников.
     */
    private void calculateBonuses() {
        for (Employee employee : employeeData) {
            double newBonus = employee.getSalary() * 0.1; // Рассчитываем премию как 10% от зарплаты
            employee.setBonus(newBonus); // Устанавливаем премию
            employeeDAO.insertBonus(employee.getId(), 2024, newBonus); // Сохраняем премию в базе данных
        }
        tableView.refresh(); // Обновляем таблицу
    }
}
