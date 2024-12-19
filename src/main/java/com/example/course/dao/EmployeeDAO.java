package com.example.course.dao;

import com.example.course.models.Employee;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    /**
     * Получение всех сотрудников из базы данных.
     *
     * @return Список объектов Employee.
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT e.id, e.first_name, e.last_name, d.name AS department_name, d.description AS position, e.salary, e.hire_date " +
                "FROM employees e " +
                "JOIN departments d ON e.department_id = d.id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String departmentName = resultSet.getString("department_name");
                String position = resultSet.getString("position"); // Получаем описание должности
                double salary = resultSet.getDouble("salary");
                LocalDate hireDate = resultSet.getDate("hire_date").toLocalDate();

                employees.add(new Employee(id, firstName, lastName, departmentName, position, salary, hireDate));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении данных о сотрудниках: " + e.getMessage());
            throw new RuntimeException("Не удалось выполнить запрос для получения сотрудников", e);
        }

        return employees;
    }



    /**
     * Вставка премии сотруднику в базу данных.
     *
     * @param employeeId  ID сотрудника.
     * @param year        Год премии.
     * @param bonusAmount Сумма премии.
     */
    public void insertBonus(int employeeId, int year, double bonusAmount) {
        String checkEmployeeQuery = "SELECT COUNT(*) FROM employees WHERE id = ?";
        String insertBonusQuery = "INSERT INTO bonuses (employee_id, year, bonus_amount) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkEmployeeQuery)) {

            checkStmt.setInt(1, employeeId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new SQLException("Сотрудник с ID " + employeeId + " не найден.");
            }

            try (PreparedStatement pstmt = conn.prepareStatement(insertBonusQuery)) {
                pstmt.setInt(1, employeeId);
                pstmt.setInt(2, year);
                pstmt.setDouble(3, bonusAmount);
                pstmt.executeUpdate();
                System.out.println("Премия успешно добавлена: Employee ID=" + employeeId + ", Year=" + year + ", Amount=" + bonusAmount);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при вставке премии в базу данных: " + e.getMessage());
            throw new RuntimeException("Не удалось вставить премию", e);
        }
    }
}
