package guis;

import DAOs.CustomerDAO;
import DAOs.StaffDAO;
import entities.Customer;
import entities.Staff;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import logic.AlertUser;
import logic.SignUp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class ManagerDashboardWindow {
    private BorderPane root;
    private VBox contentArea;
    private User currentUser;

    public ManagerDashboardWindow(User user) {
        this.currentUser = user;
    }
    Button btnStaffRecords;
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

        btnStaffRecords = createMenuButton("Staff Records");
        btnStaffRecords.setOnAction(e-> {
            TableView <Staff> s = showStaffRecords();
            fillTable(s);
        });

        Button btnUpdateStaffRecord = createMenuButton("Update Staff Record");
        btnUpdateStaffRecord.setOnAction(e->showUpdateStaff());

        Button btnCustomerRecords = createMenuButton("Customer Records");
        btnCustomerRecords.setOnAction(e->{
            TableView <Customer> c = showCustomerRecords();
            fillTableCustomer(c);
        });

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
                lblStaff, btnHire, btnFire, btnStaffRecords, btnUpdateStaffRecord, btnCustomerRecords,
                new Separator(),
                lblSystem, btnPricing, btnFloor, btnSpace,
                sep, btnSettings, btnLogout
        );

        // --- CONTENT AREA ---
        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(30));
        contentArea.setAlignment(Pos.TOP_CENTER);
        contentArea.setStyle("-fx-background-color: #ecf0f1;");

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
    private TableView<Staff> showStaffRecords(){
        TableView<Staff> table = new TableView<>();
        table.setMinHeight(400);
        table.setPrefHeight(600);
        table.setMinWidth(800);
        VBox.setVgrow(table, Priority.ALWAYS);
        TableColumn<Staff, Integer> col1 = new TableColumn<>("UserID");
        TableColumn<Staff, Integer> col2 = new TableColumn<>("Username");
        TableColumn<Staff, String> col3 = new TableColumn<>("Phone");
        TableColumn<Staff, Long> col4 = new TableColumn<>("Salary");
        TableColumn<Staff, LocalTime> col5 = new TableColumn<>("Shift Time");
        TableColumn<Staff, LocalDate> col6 = new TableColumn<>("Date Hired");
        TableColumn<Staff, String> col7 = new TableColumn<>("Job Title");

        table.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7);

        col1.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        col2.setCellValueFactory(new PropertyValueFactory<>("Username"));
        col3.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        col4.setCellValueFactory(new PropertyValueFactory<>("Salary"));
        col5.setCellValueFactory(new PropertyValueFactory<>("ShiftTime"));
        col6.setCellValueFactory(new PropertyValueFactory<>("DateHired"));
        col7.setCellValueFactory(new PropertyValueFactory<>("JobTitle"));
        return table;
    }
    private TableView<Customer> showCustomerRecords(){
        TableView<Customer> table = new TableView<>();
        table.setMinHeight(400);
        table.setPrefHeight(600);
        table.setMinWidth(800);
        VBox.setVgrow(table, Priority.ALWAYS);
        TableColumn<Customer, Integer> col1 = new TableColumn<>("UserID");
        TableColumn<Customer, String> col2 = new TableColumn<>("Username");
        TableColumn<Customer, String> col3 = new TableColumn<>("Phone");

        table.getColumns().addAll(col1, col2, col3);

        col1.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        col2.setCellValueFactory(new PropertyValueFactory<>("Username"));
        col3.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        return table;
    }
    private void fillTableCustomer(TableView<Customer> table) {
        contentArea.getChildren().clear();
        contentArea.setSpacing(15);
        Label title = new Label("Customer Management Records");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#2c3e50"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        try {
            ObservableList<Customer> data = FXCollections.observableArrayList();
            List<Customer> list = new CustomerDAO().getCustomerRecords();
            data.addAll(list);
            table.setItems(data);
        } catch (NumberFormatException f) {
            new AlertUser().showAlert(Alert.AlertType.ERROR, "Error!", f.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            new AlertUser().showAlert(Alert.AlertType.ERROR, "Search Error", "Invalid User ID or Database Error");
        }
        contentArea.getChildren().addAll(title, new Separator(), table);
    }
    private void fillTable(TableView<Staff> table) {
        contentArea.getChildren().clear();
        contentArea.setSpacing(15);
        Label title = new Label("Staff Management Records");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#2c3e50"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        try {
            ObservableList<Staff> data = FXCollections.observableArrayList();
            List<Staff> list = new StaffDAO().getStaffRecords();
            data.addAll(list);
            table.setItems(data);
        } catch (NumberFormatException f) {
            new AlertUser().showAlert(Alert.AlertType.ERROR, "Error!", f.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            new AlertUser().showAlert(Alert.AlertType.ERROR, "Search Error", "Invalid User ID or Database Error");
        }
        contentArea.getChildren().addAll(title, new Separator(), table);
    }
    Staff u;
    private void showUpdateStaff() {
        contentArea.getChildren().clear();
        Label title = new Label("Update Staff Records");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TextField idSearchField = new TextField();
        idSearchField.setPromptText("Enter UserID of staff to update: ");
        idSearchField.setMaxWidth(300);
        idSearchField.setPrefHeight(40);

        Button btnView = new Button("View Records");
        btnView.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnView.setPrefSize(200, 40);

        btnView.setOnAction(e -> {
            String inputId = idSearchField.getText();
            if (inputId.isEmpty()) {
                new AlertUser().showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a User ID.");
                return;
            }

            // RETRIEVE STAFF DATA HERE
            u = new StaffDAO().getStaffByUserId(Integer.parseInt(inputId));

            if (u == null) {
                new AlertUser().showAlert(Alert.AlertType.ERROR, "Not Found", "No staff member found with ID: " + inputId);
                return;
            }

            // If found, rebuild the contentArea with the Profile Card
            contentArea.getChildren().clear();
            contentArea.setSpacing(30);
            contentArea.setAlignment(Pos.TOP_CENTER);

            Label t = new Label("Updating Records for: " + u.getUsername());
            t.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
            t.setTextFill(Color.web("#2c3e50"));

            VBox profileCard = new VBox(20);
            profileCard.setPadding(new Insets(30));
            profileCard.setMaxWidth(500);
            profileCard.setStyle("-fx-background-color: white; " +
                    "-fx-background-radius: 10; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

            profileCard.getChildren().addAll(
                    createRecordRow("Username", u.getUsername(), "username"),
                    new Separator(),
                    createRecordRow("Phone", u.getPhone(), "phone"), // Fixed: Was showing phone for Salary label
                    new Separator(),
                    createRecordRow("Salary", String.valueOf(u.getSalary()), "salary"), // Added salary logic
                    new Separator(),
                    createRecordRow("Shift Time", String.valueOf(u.getShiftTime()), "shiftTime"),
                    new Separator(),
                    createRecordRow("Job Title", u.getJobTitle(), "jobTitle")
            );

            Button btnBack = new Button("Back to Search");
            btnBack.setOnAction(ev -> showUpdateStaff());

            contentArea.getChildren().addAll(t, profileCard, btnBack);
        });

        contentArea.getChildren().addAll(title, idSearchField, btnView);
    }
    private HBox createRecordRow(String label, String value, String type) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox textContainer = new VBox(5);
        Label lblTitle = new Label(label);
        lblTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        lblTitle.setTextFill(Color.GRAY);

        Label lblValue = new Label(value);
        lblValue.setFont(Font.font("Verdana", 15));

        textContainer.getChildren().addAll(lblTitle, lblValue);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnEdit = new Button("Edit");
        btnEdit.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");

        btnEdit.setOnAction(e -> {
            if(type.equals("shiftTime")){
                handleUpdateShiftTime(lblValue);
            }
            else if(type.equals("salary")){
                handleUpdateSalary(lblValue);
            }else {
                handleUpdate(type, lblValue);
            }
        });

        row.getChildren().addAll(textContainer, spacer, btnEdit);
        return row;
    }
    private void handleUpdateShiftTime(Label displayLabel) {
        TextInputDialog dialog = new TextInputDialog(displayLabel.getText());
        dialog.setTitle("Update Shift Time");
        dialog.setHeaderText("Change Shift Time (Format: HH:mm)");
        dialog.setContentText("Enter new Shift Time (e.g., 14:30):");

        dialog.showAndWait().ifPresent(newValue -> {
            if (!newValue.trim().isEmpty()) {
                try {
                    LocalTime newTime = java.time.LocalTime.parse(newValue.trim());

                    boolean success = new StaffDAO().updateShiftTime(newTime, currentUser.getUserID());

                    if (success) {
                        new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Success", "Shift Time Updated!");
                        displayLabel.setText(newTime.toString());

                        if (currentUser instanceof Staff) {
                            ((Staff) currentUser).setShiftTime(newTime);
                        }
                    } else {
                        new AlertUser().showAlert(Alert.AlertType.ERROR, "Error", "Database update failed!");
                    }
                } catch (java.time.format.DateTimeParseException e) {
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "Format Error", "Please use HH:mm format (e.g. 08:00)");
                }
            }
        });
    }
    private void handleUpdateSalary(Label displayLabel){
        TextInputDialog dialog = new TextInputDialog(displayLabel.getText());
        dialog.setTitle("Update Shift Time");
        dialog.setHeaderText("Change Salary");
        dialog.setContentText("Enter new Salary :");

        dialog.showAndWait().ifPresent(newValue -> {
            if (!newValue.trim().isEmpty()) {
                long newSalary = Long.parseLong(newValue);

                boolean success = new StaffDAO().updateSalary(newSalary, u.getUserID());

                if (success) {
                    new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Success", "Shift Time Updated!");
                    displayLabel.setText(String.valueOf(newSalary));

                    if (u != null) {
                        ((Staff) u).setSalary(newSalary);
                    }
                } else {
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "Error", "Database update failed!");
                }
            }else {
                new AlertUser().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid salary!");
            }
        });
    }
    private void handleUpdate(String field, Label displayLabel){
        TextInputDialog dialog = new TextInputDialog(displayLabel.getText());
        dialog.setTitle("Update " + field);
        dialog.setHeaderText("Change " + field);
        dialog.setContentText("Enter new " + field + ":");

        dialog.showAndWait().ifPresent(newValue -> {
            if (!newValue.trim().isEmpty()) {
                // Update the Database (You'll need a method in UserDAO)
                boolean success = new CustomerDAO().updateUser(field,newValue, u.getUserID());
                if (success) {
                    new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Success", "Update Successful!");
                    displayLabel.setText(newValue);
                    if (field.equals("username")) u.setUsername(newValue);
                    if (field.equals("phone")) u.setPhone(newValue);
                }
                else{
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "Error", "Error in updating information!");
                }
            }
        });
    }
    private void handleStaffSignUp(String uname, String pass, String ph, String job, String s, LocalDate hd, String shift){
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
        Staff s = new StaffDAO().getStaffByUserId(Integer.parseInt(id));
        if(s!=null && s.getJobTitle().equals("Manager")){
            new AlertUser().showAlert(Alert.AlertType.ERROR, "ERROR", "You cannot fire a manager!");
        }else {
            if (s != null) {
                boolean b = new StaffDAO().deleteStaffRecord(Integer.parseInt(id));
                if (b) {
                    new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Firing Success", "Staff " + s.getUsername() + " Successfully Fired!");
                } else {
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "ERROR", "Staff Firing Unsuccessful! Please try again.");
                }
            } else {
                new AlertUser().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid User ID!");
            }
        }
    }
    // Placeholders for your logic
    private void showRecordView(String type) { /* TableView Logic */ }
    private void showPricingConfig() { /* Pricing Form Logic */ }
}