package exceptions;
public class InvalidVehicleTypeException extends RuntimeException {
    public InvalidVehicleTypeException(String message) {
        super(message);
    }
}
