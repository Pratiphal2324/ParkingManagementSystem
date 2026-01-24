package logic;

import application.DatabaseConnection;
import entities.Customer;
import entities.Staff;
import entities.User;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class SignIn {
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Failed");
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Verdana';");

        alert.showAndWait();
    }
    public User validateLogin(String username, String password){
        if(username.isEmpty() || password.isEmpty()){
            showAlert("Invalid username or password.");
            return null;
        }
        String sql = "select userID, role,phone, count(userID) as count from user where username = ? AND password = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,username);
            pstmt.setString(2,password);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                int userID = rs.getInt("userID");
                String role = rs.getString("role");
                String phone = rs.getString("phone");
                int count = rs.getInt("count");
                if(count != 1){
                    showAlert("You are not registered. Please sign up first!");
                    return null;
                }
                if(role.equals("Customer")){
                    System.out.println("Login Successful!");
                    return new Customer(userID, username, phone, password, role);
                }
                else if(role.equals("Staff")){
                    sql = "select * from staff where userID = ?";
                    PreparedStatement pstmt2 = conn.prepareStatement(sql);
                    pstmt2.setInt(1,userID);
                    ResultSet r = pstmt2.executeQuery();
                    if(r.next()){
                        long salary = r.getInt("salary");
                        LocalTime shiftTime = r.getTime("shiftTime").toLocalTime();
                        LocalDate dateHired = r.getDate("dateHired").toLocalDate();
                        String jobTitle = r.getString("jobTitle");
                        return new Staff(userID,username,phone,password,role,salary,shiftTime,dateHired,jobTitle);
                    }
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
