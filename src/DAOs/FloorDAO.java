package DAOs;

import application.DatabaseConnection;
import entities.Floor;
import javafx.scene.control.Alert;
import logic.AlertUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FloorDAO {
    public Floor getFloorByFloorNumber(int floorNumber){
        String sql = "select * FROM view_floor WHERE floorNumber = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, floorNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int electric = rs.getInt("noOfElectricSpaces");
                int fuel = rs.getInt("noOfFuelSpaces");
                return new Floor(floorNumber,fuel,electric);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public boolean updateFloor(int floorNumber, int newElectric, int newFuel){
        String sql = "UPDATE floor SET noOfElectricSpaces = ?, noOfFuelSpaces = ?, totalNoOfSpaces = ? WHERE floorNumber = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newElectric);
            pstmt.setInt(2, newFuel);
            pstmt.setInt(3,newElectric+newFuel);
            pstmt.setInt(4,floorNumber);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public int getTotalSpaces(){
        String sql = "SELECT SUM(totalNoOfSpaces) as sum FROM view_floor";
        int n=0;
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                n = rs.getInt("sum");
            }
            else{
                new AlertUser().showAlert(Alert.AlertType.ERROR, "ERROR", "Failed to fetch total no. of spaces!");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return n;
    }
}
