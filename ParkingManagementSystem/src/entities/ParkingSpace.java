package entities;

import exceptions.InvalidVehicleCategoryException;
import exceptions.InvalidVehicleTypeException;

public class ParkingSpace {
    private final int rowNumber;
    private final int columnNumber;
    private final int floorNumber;
    private String type;
    private String category;
    private boolean isOccupied;

    public ParkingSpace(int rowNumber, int columnNumber, int floorNumber, String type, String category, boolean isOccupied)
            throws InvalidVehicleTypeException, InvalidVehicleCategoryException {

        validateType(type);
        validateCategory(category);

        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.floorNumber = floorNumber;
        this.type = type;
        this.category = category;
        this.isOccupied = isOccupied;
    }

    private void validateType(String type) throws InvalidVehicleTypeException {
        if (!(type.equalsIgnoreCase("Electric") || type.equalsIgnoreCase("Fuel"))) {
            throw new InvalidVehicleTypeException("Invalid Spot Type: " + type);
        }
    }

    private void validateCategory(String category) throws InvalidVehicleCategoryException {
        if (!(category.equalsIgnoreCase("TwoWheeler") || category.equalsIgnoreCase("FourWheeler"))) {
            throw new InvalidVehicleCategoryException("Invalid Spot Category: " + category);
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

    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }

    public int getFloorNumber() { return floorNumber; }
    public int getColumnNumber() { return columnNumber; }
    public int getRowNumber() { return rowNumber; }

    public String getSpaceID() {
        return "F" + floorNumber + "-R" + rowNumber + "-C" + columnNumber;
    }
}