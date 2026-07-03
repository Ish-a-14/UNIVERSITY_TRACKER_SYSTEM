package dao;

import db.DBConnection;
import model.Complaint;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {

    public static boolean addComplaint(int userId, String category, String desc, String priority) {

        String query = "INSERT INTO complaints(user_id, category, description, priority, status) VALUES(?,?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);
            ps.setString(2, category);
            ps.setString(3, desc);
            ps.setString(4, priority);
            ps.setString(5, "Pending");

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Complaint> getAllComplaints() {

        List<Complaint> list = new ArrayList<>();

        String query =
                "SELECT c.id AS complaint_id, c.user_id, c.category, c.description, c.priority, c.status, c.date AS complaint_date, " +
                "u.name AS user_name, u.department AS user_department " +
                "FROM complaints c " +
                "LEFT JOIN users u ON c.user_id = u.id " +
                "ORDER BY CASE c.priority " +
                "WHEN 'High' THEN 1 " +
                "WHEN 'Medium' THEN 2 " +
                "WHEN 'Low' THEN 3 " +
                "ELSE 4 END";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Complaint c = new Complaint();

                c.setId(rs.getInt("complaint_id"));
                c.setUserId(rs.getInt("user_id"));
                c.setCategory(rs.getString("category"));
                c.setDescription(rs.getString("description"));
                c.setPriority(rs.getString("priority"));
                c.setStatus(rs.getString("status"));

                c.setUserName(rs.getString("user_name"));
                c.setDepartment(rs.getString("user_department"));
                c.setDate(rs.getString("complaint_date"));

                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Complaint> getUserComplaints(int userId) {

        List<Complaint> list = new ArrayList<>();

        String query = "SELECT id, user_id, category, description, priority, status, date AS complaint_date " +
                       "FROM complaints WHERE user_id=? ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Complaint c = new Complaint(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("category"),
                            rs.getString("description"),
                            rs.getString("priority"),
                            rs.getString("status")
                    );

                    c.setDate(rs.getString("complaint_date"));
                    list.add(c);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static boolean deleteComplaint(int id, int userId) {

        String query = "DELETE FROM complaints WHERE id=? AND user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.setInt(2, userId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateComplaint(int id, int userId, String category, String desc, String priority) {

        String query = "UPDATE complaints SET category=?, description=?, priority=? WHERE id=? AND user_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, category);
            ps.setString(2, desc);
            ps.setString(3, priority);
            ps.setInt(4, id);
            ps.setInt(5, userId);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean resolveComplaint(int id) {

        String query = "UPDATE complaints SET status='Resolved' WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}