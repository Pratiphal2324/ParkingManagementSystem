package exceptions;
public class InvalidVehicleCategoryException extends RuntimeException {
    public InvalidVehicleCategoryException(String message) {
        super(message);
    }
}
