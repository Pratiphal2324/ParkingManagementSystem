package entities;

import guis.CustomerDashboardWindow;

public class Customer extends User {
    public Customer(int userID, String username, String phone, String password, String role) {
        super(userID, username, phone, password, role);
    }
    @Override
    public String getRole() {
        return "Customer";
    }
    public void showDashboard(User u, Vehicle v){
        new CustomerDashboardWindow(u,v);
    }
}
