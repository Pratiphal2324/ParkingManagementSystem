package DAOs;

import application.DatabaseConnection;
import entities.Vehicle;
import exceptions.InvalidVehicleCategoryException;
import exceptions.InvalidVehicleTypeException;
import javafx.scene.control.Alert;
import logic.AlertUser;

import java.sql.*;
import java.util.ArrayList;

public class VehicleDAO {
    public Vehicle registerVehicle(String numberPlate, String Category, String Type, int userID){
        String sql = "INSERT INTO vehicle (numberPlate,Category,Type,DriverID) VALUES (?,?,?,?)";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,numberPlate);
            pstmt.setString(2,Category);
            pstmt.setString(3,Type);
            pstmt.setInt(4,userID);
            int rows = pstmt.executeUpdate();
            if(rows>0){
                return new Vehicle(numberPlate, Category, Type, userID);
            }
            else{
                return null;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Vehicle getVehicleByNumberPlate(String numberPlate){
        String sql = "SELECT * FROM vehicle WHERE numberPlate = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,numberPlate);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                String category = rs.getString("Category");
                String type = rs.getString("Type");
                int driverID = rs.getInt("DriverID");
                try {
                    return new Vehicle(numberPlate, category, type, driverID);
                }catch(InvalidVehicleCategoryException | InvalidVehicleTypeException e){
                    new AlertUser().showAlert(Alert.AlertType.ERROR,"Error",e.getMessage());
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public ArrayList<String> getVehiclePlateByUserId(int userID){
        String sql = "SELECT numberPlate FROM vehicle WHERE DriverID = ?";
        ArrayList<String> list = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,userID);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                String plate = rs.getString("numberPlate");
                list.add(plate);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return list;
    }
    public boolean checkIfCheckedOut(Vehicle v){
        String sql = "SELECT CheckoutTime FROM vehicle INNER JOIN transaction ON numberPlate = VehiclePlate WHERE VehiclePlate = ? ORDER BY TransactionID DESC LIMIT 1";
        Timestamp timestamp;
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,v.getNumberPlate());
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                timestamp = rs.getTimestamp("checkOutTime"); // or checkInTime
                boolean b = timestamp != null;
                if(!b){
                    return false;
                }
                else{
                    return true;
                }
            }
            else{
                return true;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
