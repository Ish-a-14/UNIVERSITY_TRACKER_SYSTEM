package db;

import java.sql.*;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/complaintdb";
    private static final String USER = "root";
    private static final String PASS = "Dhruv@123"; 

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}