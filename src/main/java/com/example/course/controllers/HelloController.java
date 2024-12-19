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

    private ObservableList<Employee> employeeData = FXCollections.observableArrayList();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    @FXML
    public void initialize() {
        // Устанавливаем связь между колонками таблицы и свойствами модели Employee
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        bonusColumn.setCellValueFactory(new PropertyValueFactory<>("bonus"));

        // Добавляем обработчики событий на кнопки
        loadButton.setOnAction(event -> loadDataFromDatabase());
        calculateButton.setOnAction(event -> calculateBonuses());
    }

    /**
     * Загрузка данных сотрудников из базы данных.
     */
    private void loadDataFromDatabase() {
        if (employeeData == null) { // Проверяем, что employeeData не сброшено
            employeeData = FXCollections.observableArrayList();
        }
        employeeData.clear(); // Очищаем текущие данные
        employeeData.addAll(employeeDAO.getAllEmployees()); // Добавляем данные из базы
        tableView.setItems(employeeData); // Устанавливаем данные в таблицу
    }

    /**
     * Расчёт и сохранение премий сотрудников.
     */
    private void calculateBonuses() {
        for (Employee employee : employeeData) {
            double newBonus = employee.getSalary() * 0.1; // Рассчитываем премию как 10% от зарплаты
            employee.setBonus(newBonus); // Устанавливаем премию для отображения в таблице
            employeeDAO.insertBonus(employee.getId(), 2024, newBonus); // Сохраняем премию в базе данных
        }
        loadDataFromDatabase(); // Перезагружаем данные после сохранения премий
        tableView.refresh(); // Обновляем отображение таблицы
    }
}
