package com.example.course.dao;

import com.example.course.models.Employee;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO { // Сделаем класс public

    /**
     * Получение всех сотрудников из базы данных.
     *
     * @return Список объектов Employee.
     */
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT e.id, e.first_name, e.last_name, d.name AS department_name, d.description AS position, " +
                "e.salary, e.hire_date, e.experience_years, e.experience_months, e.performance_rating " +
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
                String position = resultSet.getString("position"); // Используем description как должность
                double salary = resultSet.getDouble("salary");
                LocalDate hireDate = resultSet.getDate("hire_date").toLocalDate();
                int experienceYears = resultSet.getInt("experience_years");
                int experienceMonths = resultSet.getInt("experience_months");
                String performance = resultSet.getString("performance_rating");

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
        String query = "INSERT INTO employees (first_name, last_name, department_id, salary, hire_date, experience_years, experience_months, performance_rating) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setInt(3, getDepartmentIdByName(employee.getDepartmentName(), connection));
            statement.setDouble(4, employee.getSalary());
            statement.setDate(5, Date.valueOf(employee.getHireDate()));
            statement.setInt(6, employee.getExperienceYears());
            statement.setInt(7, employee.getExperienceMonths());
            statement.setString(8, employee.getPerformance());

            statement.executeUpdate();

            // Получение автоматически назначенного ID
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    employee.setId(id);
                    System.out.println("Assigned ID: " + id); // Для отладки
                }
            }

            System.out.println("Сотрудник успешно добавлен: " + employee.getFirstName() + " " + employee.getLastName());
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении сотрудника: " + e.getMessage());
            throw new RuntimeException("Не удалось добавить сотрудника", e);
        }
    }




    public int getLastInsertedId() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT LASTVAL()")) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                throw new SQLException("Не удалось получить последний вставленный ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении последнего вставленного ID", e);
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
     * Обновление данных сотрудника в базе данных.
     */
    public void updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, department_id = ?, salary = ?, hire_date = ?, experience_years = ?, experience_months = ?, performance_rating = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setInt(3, getDepartmentIdByName(employee.getDepartmentName(), conn));
            pstmt.setDouble(4, employee.getSalary());
            pstmt.setDate(5, java.sql.Date.valueOf(employee.getHireDate()));
            pstmt.setInt(6, employee.getExperienceYears());
            pstmt.setInt(7, employee.getExperienceMonths());
            pstmt.setString(8, employee.getPerformance());
            pstmt.setInt(9, employee.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    /**
     * Удаление сотрудника из базы данных.
     */
    public void deleteEmployee(int employeeId) {
        String sql = "DELETE FROM employees WHERE id = ?";
        System.out.println("SQL: " + sql + " with employeeId: " + employeeId); // Временное сообщение для отладки

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, employeeId);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected); // Временное сообщение для отладки
        } catch (SQLException e) {
            e.printStackTrace();
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
