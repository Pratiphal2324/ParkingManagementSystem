package entities;

public class Customer extends User {
    public Customer(int userID, String username, String phone, String password, String role) {
        super(userID, username, phone, password, role);
    }
    @Override
    public String getRole() {
        return "Customer";
    }
}
