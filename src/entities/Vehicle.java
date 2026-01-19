package entities;

import exceptions.InvalidVehicleCategoryException;
import exceptions.InvalidVehicleTypeException;

public class Vehicle {
    private String numberPlate;
    private String category;
    private String type;
    private final int driverID;
    public Vehicle(String numberPlate, String category, String type, int driverID) throws InvalidVehicleTypeException, InvalidVehicleCategoryException {
        validateType(type);
        validateCategory(category);
        this.numberPlate = numberPlate;
        this.category = category;
        this.type = type;
        this.driverID = driverID;
    }
    private void validateType(String type) throws InvalidVehicleTypeException {
        if (!(type.equalsIgnoreCase("Electric") || type.equalsIgnoreCase("Fuel"))) {
            throw new InvalidVehicleTypeException("Type must be Electric or Fuel");
        }
    }
    private void validateCategory(String category) throws InvalidVehicleCategoryException {
        if (!(category.equalsIgnoreCase("TwoWheeler") || category.equalsIgnoreCase("FourWheeler"))) {
            throw new InvalidVehicleCategoryException("Category must be TwoWheeler or FourWheeler");
        }
    }
    public void setType(String type) throws InvalidVehicleTypeException {
        validateType(type);
        this.type = type;
    }
    public void setCategory(String category) throws InvalidVehicleCategoryException {
        validateCategory(category);
        this.category = category;
    }
    public String getNumberPlate() { return numberPlate; }
    public void setNumberPlate(String numberPlate) { this.numberPlate = numberPlate; }
    public String getCategory() { return category; }
    public String getType() { return type; }
    public int getDriverID() { return driverID; }
}