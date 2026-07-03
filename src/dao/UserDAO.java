package dao;

import db.DBConnection;
import model.User;

import java.sql.*;

public class UserDAO {

    public static boolean registerUser(String name, String email, String password, String department) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO users(name,email,password,role,department) VALUES(?,?,?,?,?)")) {

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, "faculty");
            ps.setString(5, department);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean register(User u) {
        return registerUser(
                u.getName(),
                u.getEmail(),
                u.getPassword(),
                u.getDepartment() 
        );
    }

    public static User loginUser(String email, String password) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM users WHERE email=? AND password=?")) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                User u = new User();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                u.setDepartment(rs.getString("department")); 

                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}