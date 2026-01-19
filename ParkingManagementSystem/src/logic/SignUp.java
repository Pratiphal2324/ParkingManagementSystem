package logic;

import application.DatabaseConnection;
import entities.Customer;
import entities.Staff;
import entities.User;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
public class SignUp {
    public int registerUser(String username, String phone, String password, String role) {

        String userSql = "INSERT INTO user (username, phone, password, role) VALUES (?, ?, ?, ?)";
        String customerSql = "INSERT INTO customer (userID) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, username);
                pstmt.setString(2, phone);
                pstmt.setString(3, password);
                pstmt.setString(4, role);

                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int newID = rs.getInt(1);

                            try (PreparedStatement customerPstmt = conn.prepareStatement(customerSql)) {
                                customerPstmt.setInt(1, newID);
                                customerPstmt.executeUpdate();
                            }

                            conn.commit();
                            Customer c = new Customer(newID, username, phone, password, role);
                            return c.getUserID();
                        }
                    }
                }
                else{
                    return 0;
                }
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Registration Error: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection Error: " + e.getMessage());
        }
        return 0;
    }

    public User registerUser(String username, String phone, String password, String role, long salary, LocalTime shiftTime, LocalDate dateHired, String jobTitle) {
        String userSql = "Insert into user (username, phone, password, role) values (?,?,?,?);";
        String staffSql = "Insert into staff (userID, salary, shiftTime, dateHired, jobTitle) values (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, username);
                pstmt.setString(2, phone);
                pstmt.setString(3, password);
                pstmt.setString(4, role);

                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int newID = rs.getInt(1);

                            try (PreparedStatement staffPstmt = conn.prepareStatement(staffSql)) {
                                staffPstmt.setInt(1, newID);
                                staffPstmt.setLong(2, salary);
                                staffPstmt.setTime(3, Time.valueOf(shiftTime));
                                staffPstmt.setDate(4, Date.valueOf(dateHired));
                                staffPstmt.setString(5, jobTitle);
                                staffPstmt.executeUpdate();
                            }

                            conn.commit();
                            return new Staff(newID, username, phone, password, role, salary, shiftTime, dateHired, jobTitle);
                        }
                    }
                }
                else{
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "Registration Failed", "Registration unsuccessful. Please try again");
                    return null;
                }
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Registration Error: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Connection Error: " + e.getMessage());
        }
        return null;
    }
}
