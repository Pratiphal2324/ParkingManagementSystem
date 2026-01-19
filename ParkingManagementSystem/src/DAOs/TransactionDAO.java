package DAOs;

import application.DatabaseConnection;
import entities.Transaction;
import javafx.scene.control.Alert;
import logic.AlertUser;

import java.sql.*;
import java.time.LocalDateTime;
public class TransactionDAO {
//    public Transaction getTransactionByID(int id) {
//        String sql = "SELECT * FROM transaction WHERE TransactionID = ?";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, id);
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                String plate = rs.getString("VehiclePlate");
//                int row = rs.getInt("parkingRow");
//                int col = rs.getInt("parkingColumn");
//                int floor = rs.getInt("floorNumber");
//                LocalDateTime checkIn = rs.getTimestamp("CheckinTime").toLocalDateTime();
//                int driverID = rs.getInt("driverID");
//
//                Vehicle v = new VehicleDAO().getVehicleByNumberPlate(plate);
//                ParkingSpace s = new ParkingSpaceDAO().getParkingSpaceByRowColFloor(row, col, floor);
//                Pricing p = new Pricing(v.getCategory(), v.getType());
//
//                Transaction trans = new Transaction(id, v, p, s, checkIn, driverID);
//                return trans;
//            }
//        } catch (SQLException e) {
//            System.out.println("Error fetching transaction: " + e.getMessage());
//        }
//        return null;
//    }
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

    public void updateCheckOut(Transaction trans) {
        String sql = "UPDATE transaction SET CheckoutTime = ?, totalFee = ? WHERE TransactionID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(trans.getCheckOutTime()));
            pstmt.setDouble(2, trans.getTotalFee());
            pstmt.setInt(3, trans.getTransactionID());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Check-out updated in database.");
            } else {
                System.out.println("Update failed: Transaction ID " + trans.getTransactionID() + " not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating Check-Out: " + e.getMessage());
        }
    }
    public String getTransactionByVehiclePlate(String plate){
        String sql = "SELECT * FROM transaction WHERE VehiclePlate = ? ORDER BY TransactionID DESC LIMIT 1";
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

                new ParkingSpaceDAO().updateOccupiedTo0(rNum,cNum,fNum);
                return tid + "," + fNum+ "," +rNum + "," + cNum + "," + String.valueOf(checkIn) + "," + driverID;
            }else{
                new AlertUser().showAlert(Alert.AlertType.WARNING, "Error!", "No parking space available!");
                return null;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}