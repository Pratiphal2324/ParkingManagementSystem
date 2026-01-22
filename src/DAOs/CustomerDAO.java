package DAOs;

import application.DatabaseConnection;
import entities.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public Customer getCustomerByUserId(int userid){
        String sql = "select username,phone,password,role FROM user WHERE userid = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,userid);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                String username = rs.getString("username");
                String phone = rs.getString("phone");
                String password = rs.getString("password");
                String role = rs.getString("role");
                return new Customer(userid, username, phone, password, role);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Customer getCustomerByUsernamePassword(String uname, String pass){
        if(getCustomerCountWithUsername(uname)>1){
            return null;
        }
        String sql = "select * FROM user where username = ? AND password = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,uname);
            pstmt.setString(2,pass);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                int id = rs.getInt("userID");
                String phone = rs.getString("phone");
                return new Customer(id,uname,phone,pass,"Customer");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public int getCustomerCountWithUsername(String uname){
        String sql = "select COUNT(*) as count FROM user where username = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,uname);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                return rs.getInt("count");
            }else{
                return 0;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public boolean updateUser(String s, String change, int userID){
        String sql = "";
        if(s.equals("username")){
            sql = "UPDATE user SET username = ? WHERE userID = ?";
        }
        else if(s.equals("phone")){
            sql = "UPDATE user SET phone = ? WHERE userID = ?";
        }
        else if(s.equals("password")){
            sql = "UPDATE user SET password = ? WHERE userID = ?";
        }
        else if(s.equals("jobTitle")){
            sql = "UPDATE staff SET jobTitle = ? WHERE userID = ?";
        }
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,change);
            pstmt.setInt(2,userID);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
    public List<Customer> getCustomerRecords(){
        String sql = "SELECT * FROM user WHERE role = ?";
        List<Customer> list = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,"Customer");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                int userID = rs.getInt("userID");
                String username = rs.getString("username");
                String phone = rs.getString("phone");
                String password = rs.getString("password");
                String role = rs.getString("role");
                Customer c = new Customer(userID,username,phone,password, role);
                list.add(c);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return list;
    }
}