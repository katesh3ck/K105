package com.example.course.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/bonus_system";
    private static final String USER = "postgres";
    private static final String PASSWORD = "23121204";

    /**
     * Получение соединения с базой данных.
     *
     * @return Объект Connection.
     * @throws SQLException если не удалось установить соединение.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
