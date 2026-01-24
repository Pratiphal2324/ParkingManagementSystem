package guis;

import DAOs.*;
import entities.*;
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
    private final User currentUser;

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

        Label lblWelcome = new Label("Welcome Manager, \n" + currentUser.getUsername());
        lblWelcome.setTextFill(Color.WHITE);
        lblWelcome.setFont(Font.font("Verdana", FontWeight.BOLD, 16));

        // Group 1: Personnel Management
        Label lblStaff = new Label("PERSONNEL");
        lblStaff.setTextFill(Color.web("#95a5a6"));
        lblStaff.setFont(Font.font("Arial", FontWeight.BOLD, 10));

        Button home = createMenuButton("Home");
        home.setOnAction(_ -> showWelcomeSummary());

        Button btnHire = createMenuButton("Hire Staff");
        btnHire.setOnAction(_ -> showHireStaff());

        Button btnFire = createMenuButton("Fire Staff");
        btnFire.setOnAction(_ -> showFireStaff());

        btnStaffRecords = createMenuButton("Staff Records");
        btnStaffRecords.setOnAction(_ -> {
            TableView<Staff> s = showStaffRecords();
            fillTable(s);
        });

        Button btnUpdateStaffRecord = createMenuButton("Update Staff Record");
        btnUpdateStaffRecord.setOnAction(_ -> showUpdateStaff());

        Button btnCustomerRecords = createMenuButton("Customer Records");
        btnCustomerRecords.setOnAction(_ -> {
            TableView<Customer> c = showCustomerRecords();
            fillTableCustomer(c);
        });

        // Group 2: System Management
        Label lblHome = new Label("WELCOME SCREEN");
        lblHome.setTextFill(Color.web("#95a5a6"));
        lblHome.setFont(Font.font("Arial", FontWeight.BOLD, 10));

        Label lblSystem = new Label("SYSTEM CONFIG");
        lblSystem.setTextFill(Color.web("#95a5a6"));
        lblSystem.setFont(Font.font("Arial", FontWeight.BOLD, 10));

        Button btnPricing = createMenuButton("Update Pricing");
        btnPricing.setOnAction(_ -> showUpdatePricing());

        Button btnFloor = createMenuButton("Update Floor");
        btnFloor.setOnAction(_ ->showUpdateFloor());

        Button btnSpace = createMenuButton("Update Parking Space");
        btnSpace.setOnAction(_ ->showUpdateSpace());

        Label lblPersonalConfig = new Label("PERSONAL CONFIG");
        lblPersonalConfig.setTextFill(Color.web("#95a5a6"));
        lblPersonalConfig.setFont(Font.font("Arial", FontWeight.BOLD, 10));

        // Group 3: Account
        Separator sep = new Separator();
        Button btnSettings = createMenuButton("Settings");
        Button btnLogout = createMenuButton("Logout");

        Label lblExit = new Label("EXIT");
        lblExit.setTextFill(Color.web("#95a5a6"));
        lblExit.setFont(Font.font("Arial", FontWeight.BOLD, 10));

        sidebar.getChildren().addAll(
                lblWelcome, new Separator(),
                lblHome, home, new Separator(),
                lblStaff, btnHire, btnFire, btnStaffRecords, btnUpdateStaffRecord, btnCustomerRecords,
                new Separator(),
                lblSystem, btnPricing, btnFloor, btnSpace,
                sep,lblPersonalConfig, btnSettings,new Separator(),lblExit, btnLogout
        );

        // --- CONTENT AREA ---
        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(30));
        contentArea.setAlignment(Pos.TOP_CENTER);
        contentArea.setStyle("-fx-background-color: #ecf0f1;");

        btnSettings.setOnAction(_ -> new Settings(contentArea, currentUser).showSettings());

        btnLogout.setOnAction(_ -> {
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

        double totalRevenue = new TransactionDAO().getTotalRevenue();
        int activeStaff = new StaffDAO().getTotalNoOfStaff();
        int occupancy = new ParkingSpaceDAO().getOccupancy();
        int totalParkingSpaces = new FloorDAO().getTotalSpaces();

        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        statsRow.getChildren().addAll(
                createStatCard("Total Revenue", "$" + totalRevenue, "#27ae60"),
                createStatCard("Active Staff", String.valueOf(activeStaff), "#2980b9"),
                createStatCard("Occupancy", occupancy + "/" + totalParkingSpaces, "#e67e22")
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

    private void showHireStaff() {
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
        registerBtn.setOnMouseClicked(_ -> handleStaffSignUp(staffUsername.getText(), staffPass.getText(), staffPhone.getText(), staffJobTitle.getText(), staffSalary.getText(), staffHireDate.getValue(), staffShift.getValue()));
        contentArea.getChildren().addAll(title, staffUsername, staffPass, staffJobTitle, staffSalary, staffHireDate, staffPhone, staffShift, registerBtn);
    }

    private void showFireStaff() {
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

        btnConfirm.setOnAction(_ -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Are You Sure?");
            alert.setContentText("Are you sure you want to fire a staff member?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                handleStaffFiring(staff.getText());
            }
        });

        contentArea.getChildren().addAll(title, staff, btnConfirm);

    }

    private TableView<Staff> showStaffRecords() {
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

    private TableView<Customer> showCustomerRecords() {
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
            new AlertUser().showAlert(Alert.AlertType.ERROR, "Search Error", "Invalid User ID or Database Error");
        }
        contentArea.getChildren().addAll(title, new Separator(), table);
    }

    Staff u;
    ParkingSpace parkSpace;

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

        btnView.setOnAction(_ -> {
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
            btnBack.setOnAction(_ -> showUpdateStaff());

            contentArea.getChildren().addAll(t, profileCard, btnBack);
        });

        contentArea.getChildren().addAll(title, idSearchField, btnView);
    }

    private void showUpdatePricing() {
        contentArea.getChildren().clear();
        contentArea.setSpacing(20);

        Label title = new Label("System Pricing Management");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#2c3e50"));

        // Container for the table (The Card)
        VBox tableCard = new VBox(15);
        tableCard.setPadding(new Insets(20));
        tableCard.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        TableView<Pricing> pricingTable = createPricingTable();

        // Fetch all 4 records from DB
        ObservableList<Pricing> data = FXCollections.observableArrayList(
                new PricingDAO().getPricingByTypeCategory("Fuel", "TwoWheeler"),
                new PricingDAO().getPricingByTypeCategory("Electric", "TwoWheeler"),
                new PricingDAO().getPricingByTypeCategory("Fuel", "FourWheeler"),
                new PricingDAO().getPricingByTypeCategory("Electric", "FourWheeler")
        );

        pricingTable.setItems(data);
        tableCard.getChildren().add(pricingTable);

        contentArea.getChildren().addAll(title, new Separator(), tableCard);
    }

    private void handlePriceEdit(Pricing p) {
        // 1. Create the custom dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update Pricing");
        dialog.setHeaderText("Updating Rates for: " + p.getVehicleType() + " (" + p.getVehicleCategory() + ")");

        // 2. Set the button types (Confirmation and Cancel)
        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // 3. Create the layout and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField hourlyRateField = new TextField(String.valueOf(p.getHourlyRate()));
        hourlyRateField.setPromptText("Hourly Rate");

        TextField minPriceField = new TextField(String.valueOf(p.getMinPrice()));
        minPriceField.setPromptText("Minimum Price");

        grid.add(new Label("New Hourly Rate:"), 0, 0);
        grid.add(hourlyRateField, 1, 0);
        grid.add(new Label("New Min Price:"), 0, 1);
        grid.add(minPriceField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // 4. Handle the result
        dialog.showAndWait().ifPresent(response -> {
            if (response == updateButtonType) {
                try {
                    double newHourly = Double.parseDouble(hourlyRateField.getText());
                    double newMin = Double.parseDouble(minPriceField.getText());

                    // Call your DAO to update both values
                    boolean success = new PricingDAO().updatePricing(p.getVehicleCategory(), p.getVehicleType(),newHourly,newMin);

                    if (success) {
                        new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Success", "Pricing updated successfully!");
                        showUpdatePricing(); // Refresh the table to show new values
                    }
                } catch (NumberFormatException e) {
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numeric values.");
                }
            }
        });
    }

    private TableView<Pricing> createPricingTable() {
        TableView<Pricing> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPrefHeight(250);

        TableColumn<Pricing, String> colCategory = new TableColumn<>("Category");
        colCategory.setCellValueFactory(new PropertyValueFactory<>("vehicleCategory"));

        TableColumn<Pricing, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));

        TableColumn<Pricing, Double> colHourly = new TableColumn<>("Hourly Rate ($)");
        colHourly.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));

        TableColumn<Pricing, Double> colMin = new TableColumn<>("Min Price ($)");
        colMin.setCellValueFactory(new PropertyValueFactory<>("minPrice"));

        // Add an Action Column for the Edit Button
        TableColumn<Pricing, Void> colAction = new TableColumn<>("Action");
        colAction.setCellFactory(_ -> new TableCell<>() {
            private final Button btn = new Button("Edit");

            {
                btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");
                btn.setOnAction(_ -> {
                    Pricing p = getTableView().getItems().get(getIndex());
                    handlePriceEdit(p);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().addAll(colCategory, colType, colHourly, colMin, colAction);
        return table;
    }

    private HBox createRecordRow(String label, String value, String type) {
        Label lblValue;
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        VBox textContainer = new VBox(5);
        Label lblTitle = new Label(label);
        lblTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        lblTitle.setTextFill(Color.GRAY);

        lblValue = new Label(value);
        lblValue.setFont(Font.font("Verdana", 15));

        textContainer.getChildren().addAll(lblTitle, lblValue);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnEdit = new Button("Edit");
        btnEdit.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");

        btnEdit.setOnAction(_ -> {
            switch (type) {
                case "shiftTime" -> handleUpdateShiftTime(lblValue);
                case "salary" -> handleUpdateSalary(lblValue);
                case "username", "phone", "jobTitle" -> handleUpdate(type, lblValue);
                case "category", "type" -> {
                    if (parkSpace.isOccupied()) {
                        new AlertUser().showAlert(Alert.AlertType.ERROR, "ERROR", "Cannot update parking space which is still occupied!");
                        return;
                    }
                    handleUpdateSpace(type, lblValue);
                }
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

                    boolean success = new StaffDAO().updateShiftTime(newTime, u.getUserID());

                    if (success) {
                        new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Success", "Shift Time Updated!");
                        displayLabel.setText(newTime.toString());

                        if (u != null) {
                            u.setShiftTime(newTime);
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

    private void handleUpdateSalary(Label displayLabel) {
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
                        u.setSalary(newSalary);
                    }
                } else {
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "Error", "Database update failed!");
                }
            } else {
                new AlertUser().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid salary!");
            }
        });
    }

    private void handleUpdate(String field, Label displayLabel) {
        TextInputDialog dialog = new TextInputDialog(displayLabel.getText());
        dialog.setTitle("Update " + field);
        dialog.setHeaderText("Change " + field);
        dialog.setContentText("Enter new " + field + ":");

        dialog.showAndWait().ifPresent(newValue -> {
            if (!newValue.trim().isEmpty()) {
                boolean success = new UserDAO().updateUser(field, newValue, u.getUserID());
                if (success) {
                    new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Success", "Update Successful!");
                    displayLabel.setText(newValue);
                    if (field.equals("username")) u.setUsername(newValue);
                    if (field.equals("phone")) u.setPhone(newValue);
                } else {
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "Error", "Error in updating information!");
                }
            }
        });
    }

    private void handleStaffSignUp(String uname, String pass, String ph, String job, String s, LocalDate hd, String shift) {
        int count = new CustomerDAO().getCustomerCountWithUsername(uname);
        if (count > 0) {
            new AlertUser().showAlert(Alert.AlertType.ERROR, "Username Error", "Username already taken!");
        } else {
            LocalTime startTime = switch (shift) {
                case "Morning" -> LocalTime.of(6, 0); // 06:00 AM
                case "Evening" -> LocalTime.of(14, 0); // 02:00 PM
                case "Night" -> LocalTime.of(22, 0); // 10:00 PM
                default -> LocalTime.of(9, 0);
            };
            if (!uname.isEmpty() && !pass.isEmpty()) {
                User u = new SignUp().registerUser(uname, ph, pass, "Staff", Long.parseLong(s), startTime, hd, job);
                if (u != null) {
                    new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Hiring Success", "New staff " + u.getUsername() + " Successfully Hired!");
                }
            } else {
                new AlertUser().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid Username or Password!");
            }
        }
    }

    private void handleStaffFiring(String id) {
        Staff s = new StaffDAO().getStaffByUserId(Integer.parseInt(id));
        if (s != null && s.getJobTitle().equals("Manager")) {
            new AlertUser().showAlert(Alert.AlertType.ERROR, "ERROR", "You cannot fire a manager!");
        } else {
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

    private void showUpdateFloor(){
        contentArea.getChildren().clear();
        contentArea.setSpacing(20);

        Label title = new Label("System Floor Management");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#2c3e50"));

        VBox tableCard = new VBox(15);
        tableCard.setPadding(new Insets(20));
        tableCard.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        TableView<Floor> pricingTable = createFloorTable();

        // Fetch all 4 records from DB
        ObservableList<Floor> data = FXCollections.observableArrayList(
                new FloorDAO().getFloorByFloorNumber(1),
                new FloorDAO().getFloorByFloorNumber(2),
                new FloorDAO().getFloorByFloorNumber(3),
                new FloorDAO().getFloorByFloorNumber(4)
        );

        pricingTable.setItems(data);
        tableCard.getChildren().add(pricingTable);

        contentArea.getChildren().addAll(title, new Separator(), tableCard);
    }

    private TableView<Floor> createFloorTable() {
        TableView<Floor> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPrefHeight(250);

        TableColumn<Floor, String> colCategory = new TableColumn<>("Floor no.");
        colCategory.setCellValueFactory(new PropertyValueFactory<>("FloorNumber"));

        TableColumn<Floor, String> colType = new TableColumn<>("No. of fuel spaces");
        colType.setCellValueFactory(new PropertyValueFactory<>("NoOfFuelSpaces"));

        TableColumn<Floor, Double> colHourly = new TableColumn<>("No. of electric spaces");
        colHourly.setCellValueFactory(new PropertyValueFactory<>("NoOfElectricSpaces"));

        TableColumn<Floor, Double> colMin = new TableColumn<>("Total no. of spaces");
        colMin.setCellValueFactory(new PropertyValueFactory<>("TotalNoOfSpaces"));

        // Add an Action Column for the Edit Button
        TableColumn<Floor, Void> colAction = new TableColumn<>("Action");
        colAction.setCellFactory(_ -> new TableCell<>() {
            private final Button btn = new Button("Edit");

            {
                btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");
                btn.setOnAction(_ -> {
                    Floor f = getTableView().getItems().get(getIndex());
                    handleFloorEdit(f);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        table.getColumns().addAll(colCategory, colType, colHourly, colMin, colAction);
        return table;
    }

    private void handleFloorEdit(Floor f){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Update No. of spaces");
        dialog.setHeaderText("Updating Rates for floor number: " + f.getFloorNumber());

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // 3. Create the layout and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField electricSpacesField = new TextField(String.valueOf(f.getNoOfElectricSpaces()));
        electricSpacesField.setPromptText("No. of electric spaces");

        TextField fuelSpacesField = new TextField(String.valueOf(f.getNoOfFuelSpaces()));
        fuelSpacesField.setPromptText("No. of fuel spaces");

        grid.add(new Label("New no. of electric spaces:"), 0, 0);
        grid.add(electricSpacesField, 1, 0);
        grid.add(new Label("New no. of fuel spaces:"), 0, 1);
        grid.add(fuelSpacesField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // 4. Handle the result
        dialog.showAndWait().ifPresent(response -> {
            if (response == updateButtonType) {
                try {
                    int newElectric = Integer.parseInt(electricSpacesField.getText());
                    int newFuel = Integer.parseInt(fuelSpacesField.getText());

                    // Call your DAO to update both values
                    boolean success = new FloorDAO().updateFloor(f.getFloorNumber(),newElectric,newFuel);

                    if (success) {
                        new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Success", "Floor updated successfully!");
                        showUpdateFloor(); // Refresh the table to show new values
                    }
                } catch (NumberFormatException e) {
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numeric values.");
                }
            }
        });
    }

    private void handleUpdateSpace(String field, Label displayLabel) {
        TextInputDialog dialog = new TextInputDialog(displayLabel.getText());
        dialog.setTitle("Update " + field);
        dialog.setHeaderText("Change " + field);
        dialog.setContentText("Enter new " + field + ":");
        dialog.showAndWait().ifPresent(newValue -> {
            if (!newValue.trim().isEmpty()) {
                boolean success = new ParkingSpaceDAO().updateSpace(parkSpace.getFloorNumber(),parkSpace.getRowNumber(),parkSpace.getColumnNumber(),(field.equals("category"))?newValue: parkSpace.getCategory(), (field.equals("type"))?newValue:parkSpace.getType());
                if (success) {
                    new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Success", "Update Successful!");
                    displayLabel.setText(newValue);
                    if(field.equals("category")){
                        parkSpace.setCategory(newValue);
                    }else{
                        parkSpace.setType(newValue);
                    }
                } else {
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "Error", "Error in updating information!");
                }
            }
        });
    }

    private void showUpdateSpace(){
        contentArea.getChildren().clear();
        Label title = new Label("Update parking space at: ");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TextField floorField = new TextField();
        floorField.setPromptText("Floor Number : ");
        floorField.setMaxWidth(300);
        floorField.setPrefHeight(40);

        TextField rowField = new TextField();
        rowField.setPromptText("Row Number : ");
        rowField.setMaxWidth(300);
        rowField.setPrefHeight(40);

        TextField columnField = new TextField();
        columnField.setPromptText("Column Number : ");
        columnField.setMaxWidth(300);
        columnField.setPrefHeight(40);

        Button btnView = new Button("View Parking Space Details");
        btnView.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnView.setPrefSize(200, 40);

        btnView.setOnAction(_ -> {
            int inputFloor = Integer.parseInt(floorField.getText());
            int inputRow = Integer.parseInt(rowField.getText());
            int inputColumn = Integer.parseInt(columnField.getText());
            if (floorField.getText().isEmpty() || rowField.getText().isEmpty() || columnField.getText().isEmpty()) {
                new AlertUser().showAlert(Alert.AlertType.WARNING, "Input Error", "No empty fields allowed!");
                return;
            }

            parkSpace = new ParkingSpaceDAO().getParkingSpaceByRowColFloor(inputRow,inputColumn,inputFloor);

            if (parkSpace == null) {
                new AlertUser().showAlert(Alert.AlertType.ERROR, "Not Found", "No such parking space found!");
                return;
            }
            contentArea.getChildren().clear();
            contentArea.setSpacing(30);
            contentArea.setAlignment(Pos.TOP_CENTER);

            Label t = new Label("Updating Record for parking space at Floor: "+inputFloor+", Row: "+inputRow+", Column: "+inputColumn);
            t.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
            t.setTextFill(Color.web("#2c3e50"));

            VBox profileCard = new VBox(20);
            profileCard.setPadding(new Insets(30));
            profileCard.setMaxWidth(500);
            profileCard.setStyle("-fx-background-color: white; " +
                    "-fx-background-radius: 10; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

            profileCard.getChildren().addAll(
                    createRecordRow("Vehicle Category", parkSpace.getCategory(), "category"),
                    new Separator(),
                    createRecordRow("Vehicle Type", parkSpace.getType(), "type")
            );

            Button btnBack = new Button("Back to Search");
            btnBack.setOnAction(_ -> showUpdateSpace());

            contentArea.getChildren().addAll(t, profileCard, btnBack);
        });

        contentArea.getChildren().addAll(title, floorField, rowField, columnField, btnView);
    }
}