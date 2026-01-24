package DAOs;

import application.DatabaseConnection;
import entities.Pricing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PricingDAO{
    public Pricing getPricingByTypeCategory(String type, String category){
        String sql = "select hourlyRate,MinPrice FROM view_pricing WHERE vehicleCategory = ? AND vehicleType = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            pstmt.setString(2, type);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double hourlyRate = rs.getDouble("hourlyRate");
                double minPrice = rs.getDouble("MinPrice");
                return new Pricing(category,type,hourlyRate,minPrice);
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public boolean updatePricing(String category, String type, double newRate, double newMinPrice){
        String sql = "UPDATE pricing SET hourlyRate = ?, MinPrice = ? WHERE vehicleCategory = ? AND vehicleType = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newRate);
            pstmt.setDouble(2, newMinPrice);
            pstmt.setString(3,category);
            pstmt.setString(4,type);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
