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
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        bonusColumn.setCellValueFactory(new PropertyValueFactory<>("bonus"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hireDate"));

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
            double newBonus = employee.getSalary() * 0.1; // 10% от зарплаты
            employee.setBonus(newBonus);
        }
        tableView.refresh();
    }
}
