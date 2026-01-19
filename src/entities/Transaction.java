package entities;
import java.time.*;
public class Transaction {
    public int transactionID;
    public Vehicle vehicle;
    public ParkingSpace parkingSpace;
    public Pricing pricing;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private double totalFee;
    public int driverID;
    public Transaction(int transactionID, Vehicle vehicle, Pricing pricing, ParkingSpace parkingSpace, LocalDateTime checkInTime,int driverID){
        this.transactionID = transactionID;
        this.parkingSpace = parkingSpace;
        this.pricing = pricing;
        this.vehicle = vehicle;
        this.checkInTime = checkInTime;
        this.driverID = driverID;
    }
    public void processCheckout() {
        this.checkOutTime = LocalDateTime.now();
        long minutesParked = Duration.between(checkInTime, checkOutTime).toMinutes();
        double exactHours = (double) minutesParked / 60.0;
        if (exactHours < 1.0) {
            this.totalFee = pricing.getMinPrice();
        } else {
            this.totalFee = exactHours * pricing.getHourlyRate();
        }
    }
    public LocalDateTime getCheckOutTime() {
        return LocalDateTime.now();
    }
    public int getTransactionID(){
        return transactionID;
    }
    public double getTotalFee(){
        return totalFee;
    }
    public LocalDateTime getCheckInTime(){
        return checkInTime;
    }
    public int getDriverID() { return driverID; }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }
    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public String displayBill() {
        return "TransactionID : " + transactionID + "\nDriver Id : " + driverID +"\nFloor no : " + parkingSpace.getFloorNumber() + "\nChecked in at : " + checkInTime + "\nChecked out at : " + checkOutTime + "\nVehicle Type : " + pricing.getVehicleType() + "\nVehicle Category : " + pricing.getVehicleCategory() + "\nHourly Rate : " + pricing.getHourlyRate() + "\nTotal Fee : " + String.format("%.2f", totalFee) + "\nThank you for visiting!";
    }
}
