/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.ac.tut.model.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JDBC Database Utility for Derby Database
 * 
 * @author USER
 */
public class DBConnectionUtil {
    
    private static final String URL = "jdbc:derby://localhost:1527/BabayagaCareDB";
    private static final String USER = "app";
    private static final String PASSWORD = "123";
    
    static {
        try {
            // Derby JDBC driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            System.out.println("Derby JDBC Driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("Derby JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    // Test the connection
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("Database connection successful!");
            System.out.println("Connected to: " + conn.getMetaData().getURL());
            System.out.println("Database product: " + conn.getMetaData().getDatabaseProductName());
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Main method to test connection
    public static void main(String[] args) {
        testConnection();
    }
}