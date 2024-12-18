package com.example.k105.controllers;

import com.example.k105.models.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import com.example.k105.dao.EmployeeDAO;

public class HelloController {

    @FXML private TableView<Employee> tableView; // Таблица для отображения данных
    @FXML private Button loadButton; // Кнопка загрузки данных
    @FXML private Button calculateButton; // Кнопка расчета премий

    private ObservableList<Employee> employeeData = FXCollections.observableArrayList();
    private EmployeeDAO employeeDAO = new EmployeeDAO(); // Экземпляр DAO

    @FXML
    public void initialize() {
        // Очистка старых колонок перед добавлением
        tableView.getColumns().clear();

        // Настройка колонок таблицы
        TableColumn<Employee, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Employee, String> nameColumn = new TableColumn<>("Имя сотрудника");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Employee, String> departmentColumn = new TableColumn<>("Отдел");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        TableColumn<Employee, Double> salaryColumn = new TableColumn<>("Зарплата");
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));

        TableColumn<Employee, Double> bonusColumn = new TableColumn<>("Премия");
        bonusColumn.setCellValueFactory(new PropertyValueFactory<>("bonus"));

        TableColumn<Employee, LocalDate> hireDateColumn = new TableColumn<>("Дата найма");
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hireDate"));

        // Добавляем колонки в TableView
        tableView.getColumns().addAll(idColumn, nameColumn, departmentColumn, salaryColumn, bonusColumn, hireDateColumn);

        // Настройка кнопок
        loadButton.setOnAction(event -> loadDataFromDatabase());
        calculateButton.setOnAction(event -> calculateBonuses());
    }

    private void loadDataFromDatabase() {
        employeeData.clear();
        employeeData.addAll(employeeDAO.getAllEmployees());
        tableView.setItems(employeeData);
    }

    private void calculateBonuses() {
        for (Employee employee : employeeData) {
            double newBonus = employee.getSalary() * 0.1; // Премия как 10% от зарплаты
            employee.setBonus(newBonus);
        }
        tableView.refresh(); // Обновляем таблицу
    }
}