package entities;
public class Pricing {
    private final String vehicleCategory;
    private final String vehicleType;
    private double hourlyRate;
    private double minPrice;
    public Pricing(String vehicleCategory, String vehicleType, double hourlyRate, double minPrice){
        this.vehicleCategory = vehicleCategory;
        this.vehicleType = vehicleType;
        this.hourlyRate = hourlyRate;
        this.minPrice = minPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public String getVehicleCategory() {
        return vehicleCategory;
    }

    public String getVehicleType() {
        return vehicleType;
    }
}
