package guis;

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
import javafx.scene.layout.VBox; // Changed to VBox
import javafx.scene.text.Font;
import javafx.stage.Stage;
import logic.AlertUser;

import java.time.LocalDateTime;
import java.util.List;

public class CustomerDashboardWindow {
    private Stage stage;
    private final User currentUser;
    private VBox contentArea; // Changed from StackPane to VBox

    public CustomerDashboardWindow(User user) {
        this.currentUser = user;
    }

    public void show(Stage stage) {
        this.stage = new Stage();
        this.stage.setTitle("Customer Dashboard - " + currentUser.getUsername());

        BorderPane mainLayout = new BorderPane();

        // --- SIDEBAR NAVIGATION (Updated to match Staff style) ---
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setPrefWidth(220); // Matched Staff width

        Label lblWelcome = new Label("Welcome,\n" + currentUser.getUsername());
        lblWelcome.setTextFill(javafx.scene.paint.Color.WHITE);
        lblWelcome.setFont(Font.font("Verdana", javafx.scene.text.FontWeight.BOLD, 16));

        Button btnOverview = createNavButton("My Parking Spot");
        Button btnHistory = createNavButton("My History");
        Button btnSettings = createNavButton("Settings");
        Button btnLogout = createNavButton("Logout");

        // Action Handlers
        btnOverview.setOnAction(_ -> showOverview());
        btnHistory.setOnAction(_ -> showMyHistory());
        btnSettings.setOnAction(_ -> new Settings(contentArea, currentUser).showSettings());
        btnLogout.setOnAction(_ -> {
            LoginView loginView = new LoginView();
            Scene loginScene = loginView.createLoginScene();

            Stage currentStage = (Stage) btnLogout.getScene().getWindow();
            currentStage.setScene(loginScene);
        });

        sidebar.getChildren().addAll(lblWelcome, new Separator(), btnOverview, btnHistory, btnSettings, btnLogout);

        // --- MAIN CONTENT AREA ---
        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(30)); // Matched Staff padding
        contentArea.setAlignment(Pos.TOP_CENTER); // Matched Staff alignment
        contentArea.setStyle("-fx-background-color: #ecf0f1;"); // Matched Staff background

        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(contentArea);

        // Load Default View
        showOverview();

        Scene scene = new Scene(mainLayout, 900, 600);
        this.stage.setScene(scene);
        this.stage.show();
    }

    // Updated to match Staff's createMenuButton style
    private Button createNavButton(String text) {
        Button b = new Button(text);
        b.setPrefWidth(180);
        b.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-cursor: hand;");
        return b;
    }

    // --- VIEW: OVERVIEW / ASSIGNED SPOT ---
    private void showOverview() {
        contentArea.getChildren().clear();
        VBox view = new VBox(20);
        view.setAlignment(Pos.CENTER);

        Label title = new Label("Parking Information");
        title.setFont(Font.font("Arial", 24));

        try {
            String result = new TransactionDAO().getParkedSpaceByUserId(currentUser.getUserID());
            if (result != null && !result.isEmpty()) {
                String[] s = result.split(",");
                int f = Integer.parseInt(s[0]);
                int r = Integer.parseInt(s[1]);
                int c = Integer.parseInt(s[2]);

                Label status = new Label("You are currently parked at:");
                Label spot = new Label("FLOOR: " + f + ", ROW: " + r + ", COLUMN: " + c);
                spot.setStyle("-fx-font-size: 30px; -fx-text-fill: #27ae60; -fx-font-weight: bold;");
                view.getChildren().addAll(title, status, spot);
            } else {
                view.getChildren().addAll(title, new Label("No active parking found."));
            }
        } catch (Exception e) {
            view.getChildren().add(new Label("Error loading parking data."));
        }

        contentArea.getChildren().add(view);
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
        } catch (Exception ex) {
            new AlertUser().showAlert(Alert.AlertType.ERROR, "Database Error", "Unable to load history.");
        }

        // Since contentArea is now a VBox, these will stack vertically
        contentArea.getChildren().addAll(title, table);
    }
}