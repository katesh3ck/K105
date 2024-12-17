package com.example.k105.dao;
import com.example.k105.models.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.example.k105.models.Employee;
import com.example.k105.models.Bonus;


public class EmployeeDAO {
    private static final String URL = "jdbc:postgresql://localhost:5432/bonus_system";
    private static final String USER = "postgres";
    private static final String PASSWORD = "23121204";


    // Получение всех сотрудников
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT e.id, e.name, d.name as department, e.hire_date " +
                "FROM employees e JOIN departments d ON e.department_id = d.id";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDate("hire_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    // Добавление премиальной выплаты
    public void insertBonus(int employeeId, int year, double bonusAmount) {
        String query = "INSERT INTO bonuses (employee_id, year, bonus_amount) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, employeeId);
            pstmt.setInt(2, year);
            pstmt.setDouble(3, bonusAmount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void calculateAndInsertBonuses() {
        String query = "SELECT employee_id, year, annual_result FROM employee_results";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int employeeId = rs.getInt("employee_id");
                int year = rs.getInt("year");
                double annualResult = rs.getDouble("annual_result");

                double bonus = annualResult * 0.10; // Премия = 10% от результата
                insertBonus(employeeId, year, bonus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Получение премий по сотруднику
    public List<Bonus> getBonusesByEmployee(int employeeId) {
        List<Bonus> bonuses = new ArrayList<>();
        String query = "SELECT year, bonus_amount FROM bonuses WHERE employee_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                bonuses.add(new Bonus(
                        rs.getInt("year"),
                        rs.getDouble("bonus_amount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bonuses;
    }
}
