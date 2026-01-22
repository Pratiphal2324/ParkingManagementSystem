package guis;

import DAOs.CustomerDAO;
import entities.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.AlertUser;
import logic.SignUp;

import java.time.LocalDate;
import java.time.LocalTime;

public class SignupView {

    private final VBox dynamicFieldsContainer = new VBox(15);
    Button registerBtn;
    public Scene createSignupScene(String s) {
        VBox root = new VBox(25);
        root.setPadding(new Insets(50));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2c3e50;");

        Text title = new Text("SIGN UP");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        title.setFill(Color.WHITE);

        registerBtn = new Button("CREATE ACCOUNT");
        registerBtn.setPrefWidth(300);
        registerBtn.setPrefHeight(45);
        registerBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");

        Hyperlink signinLink = new Hyperlink("Already have an account? Sign In");
        signinLink.setStyle("-fx-text-fill: #2980b9; -fx-underline: false; -fx-font-weight: bold;");
        signinLink.setOnMouseClicked(e->{
            LoginView loginView = new LoginView();
            Scene loginScene = loginView.createLoginScene();
            Stage currentStage = (Stage) signinLink.getScene().getWindow();
            currentStage.setScene(loginScene);});

        if(s.equals("Customer")){
            showCustomerFields();
        }
        else if(s.equals("Staff")){
            showStaffFields();
        }

        root.getChildren().addAll(title, dynamicFieldsContainer, registerBtn, signinLink);

        return new Scene(root, 500, 750);
    }
    TextField username;
    PasswordField pass;
    TextField phone;
    private void showCustomerFields() {
        dynamicFieldsContainer.getChildren().clear();

        username = createStyledField("Username");
        pass = createStyledPassword("Password");
        phone = createStyledField("Phone Number");
        registerBtn.setOnMouseClicked(e->handleCustomerSignUp(username.getText(),pass.getText(),phone.getText()));
        dynamicFieldsContainer.getChildren().addAll(username, pass, phone);
    }
    TextField staffUsername;
    PasswordField staffPass;
    TextField staffJobTitle;
    TextField staffSalary;
    DatePicker staffHireDate;
    ComboBox<String> staffShift;
    TextField staffPhone;
    private void showStaffFields() {
        dynamicFieldsContainer.getChildren().clear();

        staffUsername = createStyledField("Username");
        staffPass = createStyledPassword("Password");
        staffPhone = createStyledField("Phone Number");
        staffJobTitle = createStyledField("Job Title (e.g. Guard)");
        staffSalary = createStyledField("Monthly Salary");

        staffHireDate = new DatePicker();
        staffHireDate.setPromptText("Date of Hiring");
        staffHireDate.setPrefSize(300, 40);

        staffShift = new ComboBox<>();
        staffShift.getItems().addAll("Morning", "Evening", "Night");
        staffShift.setPromptText("Select Shift Time");
        staffShift.setPrefSize(300, 40);
        staffShift.setStyle("-fx-background-radius: 8;");
        registerBtn.setOnMouseClicked(e->handleStaffSignUp(staffUsername.getText(),staffPass.getText(),staffPhone.getText(),staffJobTitle.getText(),staffSalary.getText(),staffHireDate.getValue(),staffShift.getValue()));
        dynamicFieldsContainer.getChildren().addAll(staffUsername, staffPass, staffJobTitle, staffSalary, staffHireDate,staffPhone, staffShift);
    }
    public void handleStaffSignUp(String uname, String pass, String ph, String job, String s, LocalDate hd, String shift){
        int count = new CustomerDAO().getCustomerCountWithUsername(uname);
        if(count>0) {
            new AlertUser().showAlert(Alert.AlertType.ERROR,"Username Error", "Username already taken!");
        }else{
            LocalTime startTime = switch (shift) {
                case "Morning" -> LocalTime.of(6, 0); // 06:00 AM
                case "Evening" -> LocalTime.of(14, 0); // 02:00 PM
                case "Night" -> LocalTime.of(22, 0); // 10:00 PM
                default -> LocalTime.of(9, 0);
            };
            if (!uname.isEmpty() && !pass.isEmpty()) {
                User u = new SignUp().registerUser(uname, ph, pass, "Staff", Long.parseLong(s), startTime, hd, job);
                if (job.equals("Accountant")) {
                    Stage stage = (Stage) registerBtn.getScene().getWindow();
                    stage.close();
                    new StaffDashboardWindow(u).show(stage);
                } else if (job.equals("Manager")) {
                    new ManagerDashboardWindow(u);
                } else {
                    new AlertUser().showAlert(Alert.AlertType.INFORMATION, "SignUp Successful", "Successfully signed up one " + job);
                }
            }
            else{
                new AlertUser().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid Username or Password!");
            }
        }
    }
    public void handleCustomerSignUp(String uname, String pass, String ph) {
        int count = new CustomerDAO().getCustomerCountWithUsername(uname);
        if(count>0){
            new AlertUser().showAlert(Alert.AlertType.ERROR,"Username Error", "Username already taken!");
        }else {
            if (!uname.isEmpty() && !pass.isEmpty()) {
                int id = new SignUp().registerUser(uname, ph, pass, "Customer");
                if (id != 0) {
                    new AlertUser().showAlert(Alert.AlertType.INFORMATION, "SignUp Success", "New Customer Successfully Signed Up!");
                    LoginView loginView = new LoginView();
                    Scene loginScene = loginView.createLoginScene();
                    Stage currentStage = (Stage) username.getScene().getWindow();
                    currentStage.setScene(loginScene);
                } else {
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "Error", "SignUp Unsuccessful! Try again");
                }
            }
            else{
                new AlertUser().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid Username or Password!");
            }
        }
    }
    // Helper methods for consistent styling
    private TextField createStyledField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setPrefSize(300, 40);
        f.setStyle("-fx-background-radius: 8;");
        return f;
    }

    private PasswordField createStyledPassword(String prompt) {
        PasswordField p = new PasswordField();
        p.setPromptText(prompt);
        p.setPrefSize(300, 40);
        p.setStyle("-fx-background-radius: 8;");
        return p;
    }
}