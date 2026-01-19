package entities;
public class Pricing {
    private final String vehicleCategory;
    private final String vehicleType;
    private double hourlyRate;
    private double minPrice;
    public Pricing(String vehicleCategory, String vehicleType){
        this.vehicleCategory = vehicleCategory;
        this.vehicleType = vehicleType;
        setPrice();
    }

    public double getMinPrice() {
        return minPrice;
    }

    final public void setPrice() {
        if(vehicleType.equals("Electric")&&vehicleCategory.equals("TwoWheeler")){
            hourlyRate = 25.0;
            minPrice = 10.0;
        }
        else if(vehicleType.equals("Electric")&&vehicleCategory.equals("FourWheeler")){
            hourlyRate = 45.0;
            minPrice = 20.0;
        }
        else if(vehicleType.equals("Fuel")&&vehicleCategory.equals("TwoWheeler")){
            hourlyRate = 35.0;
            minPrice = 15.0;
        }
        else{
            hourlyRate = 100.0;
            minPrice = 50.0;
        }
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
