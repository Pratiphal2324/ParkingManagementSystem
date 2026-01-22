package DAOs;

import application.DatabaseConnection;
import entities.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class StaffDAO {
    public boolean deleteStaffRecord(int userID){
        String sql = "delete from user where userID = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,userID);
            int rows = pstmt.executeUpdate();
            if(rows>0){
               return true;
            }
            else{
                return false;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public Staff getStaffByUserId(int userid){
        String sql = "select username,phone,password,role,salary,shiftTime,dateHired,jobTitle FROM user INNER JOIN staff ON user.userID = staff.userID WHERE staff.userID = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,userid);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                String username = rs.getString("username");
                String phone = rs.getString("phone");
                String password = rs.getString("password");
                String role = rs.getString("role");
                long salary = rs.getLong("salary");
                LocalTime shiftTime = rs.getTime("shiftTime").toLocalTime();
                LocalDate dateHired = rs.getDate("dateHired").toLocalDate();
                String jobTitle = rs.getString("jobTitle");
                return new Staff(userid, username, phone, password, role, salary, shiftTime, dateHired, jobTitle);
            }
            else{
                return null;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
