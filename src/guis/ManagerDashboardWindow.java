package guis;

import entities.User;

import javax.swing.*;
public class ManagerDashboardWindow {
    JFrame f;
    public ManagerDashboardWindow(User u){
        f = new JFrame("Welcome Manager!");
        f.setSize(300,400);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
