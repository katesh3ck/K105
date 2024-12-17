package com.example.k105.models;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HelloController {

    @FXML private TableView<Employee> tableView; // Таблица для отображения данных
    @FXML private Button loadButton; // Кнопка загрузки данных
    @FXML private Button calculateButton; // Кнопка расчета премий

    private ObservableList<Employee> employeeData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Настройка кнопок
        loadButton.setOnAction(event -> loadDataFromDatabase());
        calculateButton.setOnAction(event -> calculateBonuses());

        // Настройка колонок таблицы
        TableColumn<Employee, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Employee, String> nameColumn = new TableColumn<>("Имя сотрудника");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Employee, String> departmentColumn = new TableColumn<>("Отдел");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        TableColumn<Employee, Double> bonusColumn = new TableColumn<>("Премия");
        bonusColumn.setCellValueFactory(new PropertyValueFactory<>("bonus"));

        // Добавляем колонки в TableView
        tableView.getColumns().addAll(idColumn, nameColumn, departmentColumn, bonusColumn);
    }

    /**
     * Метод для загрузки данных из базы данных
     */
    private void loadDataFromDatabase() {
        employeeData.clear(); // Очищаем старые данные
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/bonus_system", "postgres", "23121204")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, department, salary FROM employees");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String department = rs.getString("department");
                double salary = rs.getDouble("salary");

                double bonus = salary * 0.1; // Премия — 10% от зарплаты
                employeeData.add(new Employee(id, name, department, bonus));
            }

            tableView.setItems(employeeData); // Обновляем данные в таблице
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для пересчета премий (пример дополнительной функции)
     */
    private void calculateBonuses() {
        for (Employee employee : employeeData) {
            double newBonus = employee.getBonus() * 1.1; // Увеличиваем премию на 10%
            employee.setBonus(newBonus);
        }
        tableView.refresh(); // Обновляем отображение таблицы
    }
}
