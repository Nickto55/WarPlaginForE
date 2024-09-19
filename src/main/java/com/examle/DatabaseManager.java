package com.examle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/war_plugin_db";
    private static final String USER = "root";
    private static final String PASSWORD = "yourpassword";
    private static final String DB_NAME = "war_plugin_db";

    private Connection connection;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        createDatabaseIfNotExists();
        connection.setCatalog(DB_NAME);
        createTablesIfNotExists();
    }

    private void createDatabaseIfNotExists() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        }
    }

    private void createTablesIfNotExists() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS rulers (" +
                    "ruler_name VARCHAR(100) PRIMARY KEY, " +
                    "team_names TEXT)";
            statement.executeUpdate(createTableSQL);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
