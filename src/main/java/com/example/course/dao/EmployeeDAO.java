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
        String query = "SELECT e.id, e.first_name, e.last_name, d.name AS department_name, " +
                "d.description AS position, e.salary, e.hire_date, e.experience_years, e.experience_months " +
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
                String position = resultSet.getString("position");
                double salary = resultSet.getDouble("salary");
                LocalDate hireDate = resultSet.getDate("hire_date").toLocalDate();
                int experienceYears = resultSet.getInt("experience_years");
                int experienceMonths = resultSet.getInt("experience_months");

                employees.add(new Employee(id, firstName, lastName, departmentName, position, salary, hireDate, experienceYears, experienceMonths));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении данных о сотрудниках: " + e.getMessage());
            throw new RuntimeException("Не удалось выполнить запрос для получения сотрудников", e);
        }

        return employees;
    }

    /**
     * Добавление нового сотрудника в базу данных.
     *
     * @param employee Объект Employee, который нужно добавить.
     */
    public void insertEmployee(Employee employee) {
        String query = "INSERT INTO employees (first_name, last_name, department_id, salary, hire_date, experience_years, experience_months) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            // Здесь предполагается, что department_id уже известен:
            statement.setInt(3, getDepartmentIdByName(employee.getDepartmentName(), connection));
            statement.setDouble(4, employee.getSalary());
            statement.setDate(5, Date.valueOf(employee.getHireDate()));
            statement.setInt(6, employee.getExperienceYears());
            statement.setInt(7, employee.getExperienceMonths());

            statement.executeUpdate();
            System.out.println("Сотрудник успешно добавлен: " + employee.getFirstName() + " " + employee.getLastName());
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении сотрудника: " + e.getMessage());
            throw new RuntimeException("Не удалось добавить сотрудника", e);
        }
    }

    /**
     * Обновление стажа сотрудника.
     *
     * @param employeeId       ID сотрудника.
     * @param experienceYears  Количество лет стажа.
     * @param experienceMonths Количество месяцев стажа.
     */
    public void updateEmployeeExperience(int employeeId, int experienceYears, int experienceMonths) {
        String query = "UPDATE employees SET experience_years = ?, experience_months = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, experienceYears);
            statement.setInt(2, experienceMonths);
            statement.setInt(3, employeeId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Стаж сотрудника успешно обновлен: ID=" + employeeId);
            } else {
                System.err.println("Сотрудник с ID " + employeeId + " не найден.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении стажа сотрудника: " + e.getMessage());
            throw new RuntimeException("Не удалось обновить стаж сотрудника", e);
        }
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

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkEmployeeQuery)) {

            checkStmt.setInt(1, employeeId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                throw new SQLException("Сотрудник с ID " + employeeId + " не найден.");
            }

            try (PreparedStatement pstmt = connection.prepareStatement(insertBonusQuery)) {
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

    /**
     * Получение ID отдела по названию.
     *
     * @param departmentName Название отдела.
     * @param connection     Подключение к базе данных.
     * @return ID отдела.
     * @throws SQLException Если запрос не удался.
     */
    private int getDepartmentIdByName(String departmentName, Connection connection) throws SQLException {
        String query = "SELECT id FROM departments WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, departmentName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                throw new SQLException("Отдел с названием " + departmentName + " не найден.");
            }
        }
    }
}
