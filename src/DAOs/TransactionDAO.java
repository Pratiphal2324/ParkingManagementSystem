package DAOs;

import application.DatabaseConnection;
import entities.Pricing;
import entities.Transaction;
import entities.Vehicle;
import javafx.scene.control.Alert;
import logic.AlertUser;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public String getParkedSpaceByUserId(int id){
    String sql = "SELECT * FROM view_transaction WHERE driverID = ? AND CheckoutTime IS NULL ORDER BY TransactionID DESC LIMIT 1";
    try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
        pstmt.setInt(1,id);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            int fNum = rs.getInt("floorNumber");
            int rNum = rs.getInt("parkingRow");
            int cNum = rs.getInt("parkingColumn");
            return fNum+ "," +rNum + "," + cNum;
        }else{
            return null;
        }
    }catch(SQLException e){
        System.out.println(e.getMessage());
    }
    return null;
}
    public int saveNewTransaction(Transaction trans) {
        String sql = "INSERT INTO transaction (VehiclePlate, parkingRow, parkingColumn, floorNumber, CheckinTime, driverID) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, trans.vehicle.getNumberPlate());
            pstmt.setInt(2, trans.parkingSpace.getRowNumber());
            pstmt.setInt(3, trans.parkingSpace.getColumnNumber());
            pstmt.setInt(4, trans.parkingSpace.getFloorNumber());
            pstmt.setTimestamp(5, Timestamp.valueOf(trans.getCheckInTime()));
            pstmt.setInt(6, trans.getDriverID());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    trans.setTransactionID(id);
                    System.out.println("Check-in recorded. Database assigned ID: " + id);
                    return id;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error saving Check-In: " + e.getMessage());
        }
        return 0;
    }
    public boolean updateCheckOut(Transaction trans) {
        String sql = "UPDATE transaction SET CheckoutTime = ?, totalFee = ? WHERE TransactionID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(trans.getCheckOutTime()));
            pstmt.setDouble(2, trans.getTotalFee());
            pstmt.setInt(3, trans.getTransactionID());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Check-out updated in database.");
                return true;
            } else {
                System.out.println("Update failed: Transaction ID " + trans.getTransactionID() + " not found.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error updating Check-Out: " + e.getMessage());
        }
        return false;
    }
    public String getTransactionByVehiclePlate(String plate){
        String sql = "SELECT * FROM view_transaction WHERE VehiclePlate = ? ORDER BY TransactionID DESC LIMIT 1";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,plate);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                int tid = rs.getInt("TransactionID");
                int fNum = rs.getInt("floorNumber");
                int rNum = rs.getInt("parkingRow");
                int cNum = rs.getInt("parkingColumn");
                LocalDateTime checkIn = rs.getTimestamp("CheckinTime").toLocalDateTime();
                int driverID = rs.getInt("driverID");
                return tid + "," + fNum+ "," +rNum + "," + cNum + "," + checkIn + "," + driverID;
            }else{
                new AlertUser().showAlert(Alert.AlertType.WARNING, "Error!", "No parking space available!");
                return null;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public List<Transaction> getHistoryByUserId(int id){
        String sql = "select * from view_transaction WHERE driverID = ?";
        List<Transaction> list = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vehicle v = new VehicleDAO().getVehicleByNumberPlate(rs.getString("VehiclePlate"));
                Pricing p = new PricingDAO().getPricingByTypeCategory(v.getType(),v.getCategory());
                int r = rs.getInt("parkingRow");
                int c = rs.getInt("parkingColumn");
                int f = rs.getInt("floorNumber");
                LocalDateTime checkIn = rs.getTimestamp("CheckinTime").toLocalDateTime();
                Timestamp time = rs.getTimestamp("CheckoutTime");
                LocalDateTime checkOut = (time==null)?null : time.toLocalDateTime();
                double totalFee = rs.getDouble("TotalFee");
                Transaction t = new Transaction(
                        rs.getInt("TransactionID"),
                        v,
                        p,
                        new ParkingSpaceDAO().getParkingSpaceByRowColFloor(r,c,f),
                        checkIn,
                        rs.getInt("driverID")
                );
                t.setTotalFee(totalFee);
                t.setCheckOutTime(checkOut);
                list.add(t);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return list;
    }
    public double getTotalRevenue(){
        String sql = "SELECT SUM(TotalFee) as sum FROM view_transaction";
        double revenue = 0;
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                revenue = rs.getDouble("sum");
            }else{
                new AlertUser().showAlert(Alert.AlertType.ERROR, "ERROR", "Failed to fetch revenue!");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return revenue;
    }
    public List<Transaction> getHistoryByVehiclePlate(String plate){
        String sql = "select * from view_transaction WHERE VehiclePlate = ?";
        List<Transaction> list = new ArrayList<>();
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,plate);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vehicle v = new VehicleDAO().getVehicleByNumberPlate(plate);
                Pricing p = new PricingDAO().getPricingByTypeCategory(v.getType(),v.getCategory());
                int r = rs.getInt("parkingRow");
                int c = rs.getInt("parkingColumn");
                int f = rs.getInt("floorNumber");
                LocalDateTime checkIn = rs.getTimestamp("CheckinTime").toLocalDateTime();
                Timestamp time = rs.getTimestamp("CheckoutTime");
                LocalDateTime checkOut = (time==null)?null : time.toLocalDateTime();
                double totalFee = rs.getDouble("TotalFee");
                Transaction t = new Transaction(
                        rs.getInt("TransactionID"),
                        v,
                        p,
                        new ParkingSpaceDAO().getParkingSpaceByRowColFloor(r,c,f),
                        checkIn,
                        rs.getInt("driverID")
                );
                t.setTotalFee(totalFee);
                t.setCheckOutTime(checkOut);
                list.add(t);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return list;
    }
}