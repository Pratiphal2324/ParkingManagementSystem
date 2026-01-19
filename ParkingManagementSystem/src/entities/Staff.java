package entities;

import guis.StaffDashboardWindow;

import java.time.LocalDate;
import java.time.LocalTime;

public class Staff extends User{
    long salary;
    LocalTime shiftTime;
    LocalDate dateHired;
    String jobTitle;
    public Staff(int userID, String username, String phone, String password,String role, long salary, LocalTime shiftTime, LocalDate dateHired, String jobTitle){
        super(userID, username, phone, password,role);
        this.salary = salary;
        this.shiftTime = shiftTime;
        this.dateHired = dateHired;
        this.jobTitle = jobTitle;
    }
    public void setSalary(long salary){
        this.salary = salary;
    }
    public long getSalary(){
        return salary;
    }

    public void setShiftTime(LocalTime shiftTime) {
        this.shiftTime = shiftTime;
    }

    public LocalTime getShiftTime(){
        return shiftTime;
    }

    public void setDateHired(LocalDate dateHired) {
        this.dateHired = dateHired;
    }

    public LocalDate getDateHired(){
        return dateHired;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobTitle(){
        return jobTitle;
    }
    public void showDashboard(User u){
        new StaffDashboardWindow(u);
    }
}
