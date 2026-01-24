package DAOs;

import application.DatabaseConnection;
import entities.Staff;
import javafx.scene.control.Alert;
import logic.AlertUser;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {
    public List<Staff> getStaffRecords() {
        String sql = "SELECT * FROM user INNER JOIN staff ON user.userID = staff.userID";
        List<Staff> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int userID = rs.getInt("userID");
                String username = rs.getString("username");
                String phone = rs.getString("phone");
                String password = rs.getString("password");
                String role = rs.getString("role");
                long salary = rs.getLong("salary");
                LocalTime shiftTime = rs.getTime("shiftTime").toLocalTime();
                LocalDate dateHired = rs.getDate("dateHired").toLocalDate();
                String jobTitle = rs.getString("jobTitle");
                Staff s = new Staff(userID, username, phone, password, role, salary, shiftTime, dateHired, jobTitle);
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public boolean deleteStaffRecord(int userID) {
        String sql = "delete from user where userID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userID);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Staff getStaffByUserId(int userid) {
        String sql = "select username,phone,password,role,salary,shiftTime,dateHired,jobTitle FROM user INNER JOIN staff ON user.userID = staff.userID WHERE staff.userID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userid);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                String phone = rs.getString("phone");
                String password = rs.getString("password");
                String role = rs.getString("role");
                long salary = rs.getLong("salary");
                LocalTime shiftTime = rs.getTime("shiftTime").toLocalTime();
                LocalDate dateHired = rs.getDate("dateHired").toLocalDate();
                String jobTitle = rs.getString("jobTitle");
                return new Staff(userid, username, phone, password, role, salary, shiftTime, dateHired, jobTitle);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean updateSalary(long change, int userID) {
        String sql = "UPDATE staff SET salary = ? WHERE userID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, change);
            pstmt.setInt(2, userID);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean updateShiftTime(LocalTime change, int userID) {
        String sql = "UPDATE staff SET shiftTime = ? WHERE userID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTime(1, Time.valueOf(change));
            pstmt.setInt(2, userID);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int getTotalNoOfStaff() {
        String sql = "SELECT COUNT(userID) as count FROM view_staff";
        int n = 0;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                n = rs.getInt("count");
            } else {
                new AlertUser().showAlert(Alert.AlertType.ERROR, "ERROR", "Failed to fetch no. of active staff!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return n;
    }
}
