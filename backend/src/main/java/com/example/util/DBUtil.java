package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    // Static initializer block to load the driver
    static {
        try {
            // Load the MySQL JDBC driver class
            // Note: The class name changed in newer versions of the driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Handle the error appropriately - maybe log it or throw a runtime exception
            System.err.println("Failed to load MySQL JDBC driver: " + e.getMessage());
            // Depending on your error handling strategy, you might want to re-throw
            // throw new RuntimeException("Failed to load MySQL JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String host = System.getenv("MYSQL_HOST");
        String db = System.getenv("MYSQL_DATABASE");
        String user = System.getenv("MYSQL_USER");
        String pass = System.getenv("MYSQL_PASSWORD");
        // Ensure environment variables are not null or empty if needed
        if (host == null || db == null || user == null || pass == null) {
            throw new SQLException("Database configuration environment variables are not set properly.");
        }
        String url = "jdbc:mysql://" + host + ":3306/" + db + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        return DriverManager.getConnection(url, user, pass);
    }
}
