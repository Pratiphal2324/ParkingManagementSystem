package entities;
public class Floor {
    private int floorNumber;
    private int noOfElectricSpaces;
    private int noOfFuelSpaces;
    private int totalNoOfSpaces;
    public Floor(int floorNumber, int noOfFuelSpaces, int noOfElectricSpaces){
        this.floorNumber = floorNumber;
        this.noOfFuelSpaces = noOfFuelSpaces;
        this.noOfElectricSpaces = noOfElectricSpaces;
        this.totalNoOfSpaces = noOfElectricSpaces + noOfFuelSpaces;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public int getNoOfElectricSpaces() {
        return noOfElectricSpaces;
    }

    public void setNoOfElectricSpaces(int noOfElectricSpaces) {
        this.noOfElectricSpaces = noOfElectricSpaces;
        this.totalNoOfSpaces = noOfElectricSpaces + noOfFuelSpaces;
    }

    public void setNoOfFuelSpaces(int noOfFuelSpaces) {
        this.noOfFuelSpaces = noOfFuelSpaces;
        this.totalNoOfSpaces = noOfElectricSpaces + noOfFuelSpaces;
    }

    public int getNoOfFuelSpaces() {
        return noOfFuelSpaces;
    }
    public int getTotalNoOfSpaces() {
        return totalNoOfSpaces;
    }
}
