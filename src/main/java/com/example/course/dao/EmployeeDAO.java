package com.example.course.dao;

import com.example.course.models.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/bonus_system";
    private static final String USER = "postgres";
    private static final String PASSWORD = "23121204";

    /**
     * Получение всех сотрудников из базы данных.
     *
     * @return Список объектов Employee.
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT e.id, e.name, d.name as department, e.salary, e.hire_date " +
                "FROM employees e JOIN departments d ON e.department_id = d.id";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"),
                        rs.getDate("hire_date").toLocalDate()
                ));
            }
            System.out.println("Успешно загружены данные о сотрудниках из базы.");
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

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
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
