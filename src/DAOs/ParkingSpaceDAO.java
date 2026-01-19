package DAOs;

import application.DatabaseConnection;
import entities.ParkingSpace;
import entities.Vehicle;
import javafx.scene.control.Alert;
import logic.AlertUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class ParkingSpaceDAO {
    public ParkingSpace getParkingSpaceByRowColFloor(int row, int column, int floorNumber){
        String sql = "SELECT * FROM parkingspace WHERE RowNumber = ? AND ColumnNumber = ? AND FloorNumber = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,row);
            pstmt.setInt(2,column);
            pstmt.setInt(3,floorNumber);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                String type = rs.getString("Type");
                String category = rs.getString("Category");
                int isOccupied = rs.getInt("isOccupied");
                return new ParkingSpace(row,column,floorNumber,type, category, isOccupied == 1);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public String assignParkingSpace(Vehicle v){
        boolean b = new VehicleDAO().checkIfCheckedOut(v);
        if(!b){
            return null;
        }
        String sql = "SELECT * FROM parkingspace WHERE Type = ? AND Category = ? AND isOccupied = 0 ORDER BY FloorNumber ASC, RowNumber ASC, ColumnNumber ASC LIMIT 1";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,v.getType());
            pstmt.setString(2,v.getCategory());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                int fNum = rs.getInt("FloorNumber");
                int rNum = rs.getInt("RowNumber");
                int cNum = rs.getInt("ColumnNumber");
                updateOccupiedTo1(rNum,cNum,fNum);
                return fNum+ "," +rNum + "," + cNum;
            }else{
                new AlertUser().showAlert(Alert.AlertType.WARNING, "Error!", "No parking space available!");
                return null;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void updateOccupiedTo1(int row, int column, int floorNumber){
        String sql = "UPDATE parkingspace SET isOccupied = 1 WHERE RowNumber = ? AND ColumnNumber = ? AND FloorNumber = ? AND isOccupied = 0";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,row);
            pstmt.setInt(2,column);
            pstmt.setInt(3,floorNumber);
            int rows = pstmt.executeUpdate();
            if(rows == 1){
                System.out.println("parkingSpace successfully occupied");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void updateOccupiedTo0(int row, int column, int floorNumber){
        String sql = "UPDATE parkingspace SET isOccupied = 0 WHERE RowNumber = ? AND ColumnNumber = ? AND FloorNumber = ? AND isOccupied = 1";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,row);
            pstmt.setInt(2,column);
            pstmt.setInt(3,floorNumber);
            int rows = pstmt.executeUpdate();
            if(rows == 1){
                System.out.println("parkingSpace successfully occupied");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
