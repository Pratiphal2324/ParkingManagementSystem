package guis;

import DAOs.CustomerDAO;
import DAOs.TransactionDAO;
import entities.Transaction;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import logic.AlertUser;

import java.time.LocalDateTime;
import java.util.List;

public class CustomerDashboardWindow {
    private Stage stage;
    private User currentUser;
    private StackPane contentArea;

    public CustomerDashboardWindow(User user) {
        this.currentUser = user;
    }

    public void show(Stage stage) {
        stage = new Stage();
        stage.setTitle("Customer Dashboard - " + currentUser.getUsername());

        BorderPane mainLayout = new BorderPane();

        // --- SIDEBAR NAVIGATION ---
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setPrefWidth(200);

        Label lblUser = new Label("Welcome, " + currentUser.getUsername());
        lblUser.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button btnOverview = createNavButton("My Parking Spot");
        Button btnHistory = createNavButton("My History");
        Button btnSettings = createNavButton("Settings");
        Button btnLogout = createNavButton("Logout");

        // Action Handlers
        btnOverview.setOnAction(e -> showOverview());
        btnHistory.setOnAction(e -> showMyHistory());
        btnSettings.setOnAction(e->showSettings());
        btnLogout.setOnAction(e -> {
            LoginView loginView = new LoginView();
            Scene loginScene = loginView.createLoginScene();

            Stage currentStage = (Stage) btnLogout.getScene().getWindow();
            currentStage.setScene(loginScene);// Assuming you have a LoginWindow
        });

        sidebar.getChildren().addAll(lblUser, new Separator(), btnOverview, btnHistory, btnSettings, btnLogout);

        // --- MAIN CONTENT AREA ---
        contentArea = new StackPane();
        contentArea.setPadding(new Insets(20));

        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(contentArea);

        // Load Default View
        showOverview();

        Scene scene = new Scene(mainLayout, 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-alignment: CENTER_LEFT;");
        return btn;
    }

    // --- VIEW: OVERVIEW / ASSIGNED SPOT ---
    private void showOverview() {
        contentArea.getChildren().clear();
        VBox view = new VBox(20);
        view.setAlignment(Pos.CENTER);

        Label title = new Label("Parking Information");
        title.setFont(Font.font("Arial", 24));

        // Logic: Check if user is currently parked
        // You'll need to implement getActiveTransactionByUserId in your TransactionDAO
        String [] s = new TransactionDAO().getParkedSpaceByUserId(currentUser.getUserID()).split(",");
        int f = Integer.parseInt(s[0]);
        int r = Integer.parseInt(s[1]);
        int c = Integer.parseInt(s[2]);
        Label status = new Label("You are currently parked at:");
        Label spot = new Label("FLOOR :" + f + ", ROW : "+r+", COLUMN : "+c); // Replace with real data
        spot.setStyle("-fx-font-size: 30px; -fx-text-fill: #27ae60; -fx-font-weight: bold;");
        view.getChildren().addAll(title, status, spot);
        contentArea.getChildren().add(view);
    }
    private void showSettings() {
        contentArea.getChildren().clear();
        VBox view = new VBox(20);
        view.setPadding(new Insets(20));
        view.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Account Settings");
        title.setFont(Font.font("Arial", 24));
        title.setStyle("-fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        // --- USERNAME SECTION ---
        Label lblUserTag = new Label("Username:");
        Label lblUsername = new Label(currentUser.getUsername());
        Button btnEditUser = new Button("Change");
        btnEditUser.setOnAction(e -> handleUpdate("username", lblUsername));

        // --- PHONE SECTION ---
        Label lblPhoneTag = new Label("Phone Number:");
        Label lblPhone = new Label(currentUser.getPhone()); // Assuming getPhone() exists
        Button btnEditPhone = new Button("Change");
        btnEditPhone.setOnAction(e -> handleUpdate("phone", lblPhone));

        // --- PASSWORD SECTION ---
        Label lblPassTag = new Label("Password:");
        Label lblPassDots = new Label("********");
        Button btnEditPass = new Button("Change");
        btnEditPass.setOnAction(e -> handlePasswordChange());

        // Add to grid (Column, Row)
        grid.add(lblUserTag, 0, 0);
        grid.add(lblUsername, 1, 0);
        grid.add(btnEditUser, 2, 0);

        grid.add(lblPhoneTag, 0, 1);
        grid.add(lblPhone, 1, 1);
        grid.add(btnEditPhone, 2, 1);

        grid.add(lblPassTag, 0, 2);
        grid.add(lblPassDots, 1, 2);
        grid.add(btnEditPass, 2, 2);

        view.getChildren().addAll(title, new Separator(), grid);
        contentArea.getChildren().add(view);
    }
    private void handleUpdate(String field, Label displayLabel){
        TextInputDialog dialog = new TextInputDialog(displayLabel.getText());
        dialog.setTitle("Update " + field);
        dialog.setHeaderText("Change your " + field);
        dialog.setContentText("Enter new " + field + ":");

        dialog.showAndWait().ifPresent(newValue -> {
            if (!newValue.trim().isEmpty()) {
                // Update the Database (You'll need a method in UserDAO)
                boolean success = new CustomerDAO().updateUser(field,newValue, currentUser.getUserID());
                if (success) {
                    new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Success", "Update Successful!");
                    displayLabel.setText(newValue);
                    if (field.equals("username")) currentUser.setUsername(newValue);
                    if (field.equals("phone")) currentUser.setPhone(newValue);
                }
                else{
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "Error", "Error in updating information!");
                }
            }
        });
    }
    private void handlePasswordChange(){
        contentArea.getChildren().clear();
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        PasswordField pfCurrent = new PasswordField();
        PasswordField pfNew = new PasswordField();
        PasswordField pfNewConfirm = new PasswordField();
        pfCurrent.setPromptText("Enter current password : ");
        pfNew.setPromptText("Enter new password : ");
        pfNewConfirm.setPromptText("Confirm new password : ");
        Button btn = new Button("Change Password");
        grid.add(pfCurrent, 0, 0);
        grid.add(pfNew, 0, 1);
        grid.add(pfNewConfirm, 0, 2);
        grid.add(btn, 0, 3);
        btn.setOnAction(e->{
            boolean success = new CustomerDAO().updateUser("password", pfNew.getText(), currentUser.getUserID());
            boolean confirmedPassword = pfNew.getText().equals(pfNewConfirm.getText());
            if(confirmedPassword) {
                if (success) {
                    new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Success", "Password Update Successful!");
                } else {
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "ERROR", "Password Update Error!");
                }
            }else{
                new AlertUser().showAlert(Alert.AlertType.ERROR, "ERROR", "Passwords do not match! Try again!");
            }
        });
        contentArea.getChildren().addAll(grid);
    }
    // --- VIEW: TRANSACTION HISTORY ---
    private void showMyHistory() {
        contentArea.getChildren().clear();
        Label title = new Label("My Transaction History: ");
        title.setFont(Font.font("Arial", 22));

        TableView<Transaction> table = new TableView<>();
        TableColumn<Transaction, Integer> col1 = new TableColumn<>("TransactionID");
        TableColumn<Transaction, Integer> col2 = new TableColumn<>("driverID");
        TableColumn<Transaction, LocalDateTime> col3 = new TableColumn<>("CheckinTime");
        TableColumn<Transaction, LocalDateTime> col4 = new TableColumn<>("CheckoutTime");
        TableColumn<Transaction, String> col5 = new TableColumn<>("VehiclePlate");
        TableColumn<Transaction, Integer> col6 = new TableColumn<>("Floor");
        TableColumn<Transaction, Integer> col7 = new TableColumn<>("Row");
        TableColumn<Transaction, Integer> col8 = new TableColumn<>("Column");
        TableColumn<Transaction, Double> col9 = new TableColumn<>("TotalFee");

        table.getColumns().addAll(col1, col2, col3, col4, col5, col6, col7, col8, col9);

        col1.setCellValueFactory(new PropertyValueFactory<>("TransactionID"));
        col2.setCellValueFactory(new PropertyValueFactory<>("driverID"));
        col3.setCellValueFactory(new PropertyValueFactory<>("checkInTime"));
        col4.setCellValueFactory(new PropertyValueFactory<>("checkOutTime"));
        col5.setCellValueFactory(new PropertyValueFactory<>("VehiclePlate"));
        col6.setCellValueFactory(new PropertyValueFactory<>("FloorNumber"));
        col7.setCellValueFactory(new PropertyValueFactory<>("ParkingRow"));
        col8.setCellValueFactory(new PropertyValueFactory<>("ParkingColumn"));
        col9.setCellValueFactory(new PropertyValueFactory<>("TotalFee"));
            try {
                ObservableList<Transaction> data = FXCollections.observableArrayList();
                List<Transaction> list = new TransactionDAO().getHistoryByUserId(currentUser.getUserID());
                data.addAll(list);
                table.setItems(data);
            }catch(NumberFormatException f){
                new AlertUser().showAlert(Alert.AlertType.ERROR, "Error!", f.getMessage());
            }
            catch (Exception ex) {
                ex.printStackTrace();
                new AlertUser().showAlert(Alert.AlertType.ERROR, "Search Error", "Invalid User ID or Database Error");
            }
        contentArea.getChildren().addAll(title,table);
    }
}