package DAOs;

import application.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    public boolean updateUser(String s, String change, int userID){
        String sql = switch (s) {
            case "username" -> "UPDATE user SET username = ? WHERE userID = ?";
            case "phone" -> "UPDATE user SET phone = ? WHERE userID = ?";
            case "password" -> "UPDATE user SET password = ? WHERE userID = ?";
            case "jobTitle" -> "UPDATE staff SET jobTitle = ? WHERE userID = ?";
            default -> "";
        };
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
}
