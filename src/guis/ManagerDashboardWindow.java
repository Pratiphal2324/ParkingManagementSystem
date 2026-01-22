package guis;

import DAOs.CustomerDAO;
import DAOs.StaffDAO;
import entities.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import logic.AlertUser;
import logic.SignUp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public class ManagerDashboardWindow {
    private BorderPane root;
    private VBox contentArea;
    private User currentUser;

    public ManagerDashboardWindow(User user) {
        this.currentUser = user;
    }

    public void show(Stage stage) {
        root = new BorderPane();

        // --- SIDEBAR (Same Style as Staff) ---
        VBox sidebar = new VBox(12);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setPrefWidth(220);

        Label lblWelcome = new Label("Manager Portall\n" + currentUser.getUsername());
        lblWelcome.setTextFill(Color.WHITE);
        lblWelcome.setFont(Font.font("Verdana", FontWeight.BOLD, 16));

        // Group 1: Personnel Management
        Label lblStaff = new Label("PERSONNEL");
        lblStaff.setTextFill(Color.web("#95a5a6"));
        lblStaff.setFont(Font.font("Arial", FontWeight.BOLD, 10));

        Button btnHire = createMenuButton("Hire Staff");
        btnHire.setOnAction(e->showHireStaff());
        Button btnFire = createMenuButton("Fire Staff");
        btnFire.setOnAction(e-> showFireStaff());
        Button btnStaffRecords = createMenuButton("Staff Records");
        Button btnCustomerRecords = createMenuButton("Customer Records");

        // Group 2: System Management
        Label lblSystem = new Label("SYSTEM CONFIG");
        lblSystem.setTextFill(Color.web("#95a5a6"));
        lblSystem.setFont(Font.font("Arial", FontWeight.BOLD, 10));

        Button btnPricing = createMenuButton("Update Pricing");
        Button btnFloor = createMenuButton("Update Floor");
        Button btnSpace = createMenuButton("Update Parking Space");

        // Group 3: Account
        Separator sep = new Separator();
        Button btnSettings = createMenuButton("Settings");
        Button btnLogout = createMenuButton("Logout");

        sidebar.getChildren().addAll(
                lblWelcome, new Separator(),
                lblStaff, btnHire, btnFire, btnStaffRecords, btnCustomerRecords,
                new Separator(),
                lblSystem, btnPricing, btnFloor, btnSpace,
                sep, btnSettings, btnLogout
        );

        // --- CONTENT AREA ---
        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(30));
        contentArea.setAlignment(Pos.TOP_CENTER);
        contentArea.setStyle("-fx-background-color: #ecf0f1;");

        // --- EVENT HANDLERS ---
        btnStaffRecords.setOnAction(e -> showRecordView("Staff Members"));
        btnCustomerRecords.setOnAction(e -> showRecordView("Registered Customers"));
        btnPricing.setOnAction(e -> showPricingConfig());
        btnSettings.setOnAction(e -> new Settings(contentArea, currentUser).showSettings());

        btnLogout.setOnAction(e -> {
            LoginView loginView = new LoginView();
            Scene loginScene = loginView.createLoginScene();
            Stage currentStage = (Stage) btnLogout.getScene().getWindow();
            currentStage.setScene(loginScene);
        });

        root.setLeft(sidebar);
        root.setCenter(contentArea);

        // Default view
        showWelcomeSummary();

        Scene scene = new Scene(root, 1100, 700); // Slightly wider for manager data
        stage.setTitle("Admin Control Panel");
        stage.setScene(scene);
        stage.show();
    }

    private Button createMenuButton(String text) {
        Button b = new Button(text);
        b.setPrefWidth(180);
        b.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-cursor: hand; -fx-alignment: CENTER_LEFT;");
        return b;
    }

    private void showWelcomeSummary() {
        contentArea.getChildren().clear();
        Label title = new Label("Administrative Overview");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));

        // Example "Stats Card" Row
        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        statsRow.getChildren().addAll(
                createStatCard("Total Revenue", "$12,450", "#27ae60"),
                createStatCard("Active Staff", "8", "#2980b9"),
                createStatCard("Occupancy", "74%", "#e67e22")
        );

        contentArea.getChildren().addAll(title, new Separator(), statsRow);
    }

    private VBox createStatCard(String title, String val, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setPrefSize(200, 100);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label lblT = new Label(title);
        lblT.setTextFill(Color.GRAY);
        Label lblV = new Label(val);
        lblV.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblV.setTextFill(Color.web(color));

        card.getChildren().addAll(lblT, lblV);
        return card;
    }
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
    Button registerBtn;
    private void showHireStaff(){
        contentArea.getChildren().clear();
        Label title = new Label("Hire New Staff");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        TextField staffUsername = createStyledField("Username");
        TextField staffPass = createStyledPassword("Password");
        TextField staffPhone = createStyledField("Phone Number");
        TextField staffJobTitle = createStyledField("Job Title (e.g. Guard)");
        TextField staffSalary = createStyledField("Monthly Salary");

        DatePicker staffHireDate = new DatePicker();
        staffHireDate.setPromptText("Date of Hiring");
        staffHireDate.setPrefSize(300, 40);

        ComboBox<String> staffShift = new ComboBox<>();
        staffShift.getItems().addAll("Morning", "Evening", "Night");
        staffShift.setPromptText("Select Shift Time");
        staffShift.setPrefSize(300, 40);
        staffShift.setStyle("-fx-background-radius: 8;");
        registerBtn = new Button("Hire");
        registerBtn.setOnMouseClicked(e->handleStaffSignUp(staffUsername.getText(),staffPass.getText(),staffPhone.getText(),staffJobTitle.getText(),staffSalary.getText(),staffHireDate.getValue(),staffShift.getValue()));
        contentArea.getChildren().addAll(title, staffUsername, staffPass, staffJobTitle, staffSalary, staffHireDate,staffPhone, staffShift, registerBtn);
    }
    private void showFireStaff(){
        contentArea.getChildren().clear();
        Label title = new Label("Fire Staff");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TextField staff = new TextField();
        staff.setPromptText("Enter UserID of staff to fire: ");
        staff.setMaxWidth(300);
        staff.setPrefHeight(40);

        Button btnConfirm = new Button("Fire");
        btnConfirm.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnConfirm.setPrefSize(200, 40);

        btnConfirm.setOnAction(e-> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Are You Sure?");
            alert.setContentText("Are you sure you want to fire a staff member?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                handleStaffFiring(staff.getText());
            }
        });

        contentArea.getChildren().addAll(title, staff, btnConfirm);

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
                if(u!=null) {
                    new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Hiring Success", "New staff " + u.getUsername() + "Successfully Hired!");
                }
            }
            else{
                new AlertUser().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid Username or Password!");
            }
        }
    }
    private void handleStaffFiring(String id){
        User u = new StaffDAO().getStaffByUserId(Integer.parseInt(id));
        if(u!=null) {
            boolean b = new StaffDAO().deleteStaffRecord(Integer.parseInt(id));
            if (b) {
                new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Firing Success", "Staff " + u.getUsername() + " Successfully Fired!");
            }
            else{
                new AlertUser().showAlert(Alert.AlertType.ERROR, "ERROR", "Staff Firing Unsuccessful! Please try again.");
            }
        }
        else{
            new AlertUser().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid User ID!");
        }
    }
    // Placeholders for your logic
    private void showRecordView(String type) { /* TableView Logic */ }
    private void showPricingConfig() { /* Pricing Form Logic */ }
}