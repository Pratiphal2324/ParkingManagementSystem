package guis;

import entities.User;
import entities.Vehicle;

import javax.swing.*;
public class CustomerDashboardWindow {
    JFrame f;
    public CustomerDashboardWindow(User u, Vehicle v){
        f = new JFrame("Welcome");
        f.setSize(300,400);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
