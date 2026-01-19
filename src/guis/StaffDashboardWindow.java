package guis;

import DAOs.ParkingSpaceDAO;
import DAOs.TransactionDAO;
import DAOs.VehicleDAO;
import entities.Pricing;
import entities.Transaction;
import entities.User;
import entities.Vehicle;
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

import java.time.LocalDateTime;

public class StaffDashboardWindow {
    private BorderPane root;
    private VBox contentArea;
    private User currentUser;

    public StaffDashboardWindow(User user) {
        this.currentUser = user;
    }

    public void show(Stage stage) {
        root = new BorderPane();

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #2c3e50;");
        sidebar.setPrefWidth(220);

        Label lblWelcome = new Label("Welcome,\n" + currentUser.getUsername());
        lblWelcome.setTextFill(Color.WHITE);
        lblWelcome.setFont(Font.font("Verdana", FontWeight.BOLD, 16));

        Button btnCheckIn = createMenuButton("Check In Vehicle");
        Button btnCheckOut = createMenuButton("Check Out Vehicle");
        Button btnHistoryUser = createMenuButton("History by UserID");
        Button btnHistoryPlate = createMenuButton("History by Plate");
        Button btnLogout = createMenuButton("Logout");

        sidebar.getChildren().addAll(lblWelcome, new Separator(), btnCheckIn, btnCheckOut, btnHistoryUser, btnHistoryPlate, btnLogout);

        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(30));
        contentArea.setAlignment(Pos.TOP_CENTER);
        contentArea.setStyle("-fx-background-color: #ecf0f1;");

        btnCheckIn.setOnAction(e -> showCheckInView()); // Link to new method
        btnCheckOut.setOnAction(e -> showCheckOutView());
        btnHistoryUser.setOnAction(e -> showHistoryView("Search by User ID", "Enter ID..."));
        btnHistoryPlate.setOnAction(e -> showHistoryView("Search by Plate", "Enter Plate..."));

        root.setLeft(sidebar);
        root.setCenter(contentArea);

        showCheckInView();

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Staff Dashboard - Parking System");
        stage.setScene(scene);
        stage.show();
    }

    private void showCheckInView() {
        contentArea.getChildren().clear();
        Label title = new Label("Vehicle Check-In");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TextField plateField = new TextField();
        plateField.setPromptText("Enter Plate Number to Check-In");
        plateField.setMaxWidth(300);
        plateField.setPrefHeight(40);

        TextField driverIdField = new TextField();
        driverIdField.setPromptText("Enter Driver ID:");
        driverIdField.setMaxWidth(300);
        driverIdField.setPrefHeight(40);

        Button btnConfirmIn = new Button("Confirm Entry");
        btnConfirmIn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;");
        btnConfirmIn.setPrefSize(200, 40);

        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("Verdana", 14));

        btnConfirmIn.setOnAction(e -> {
            String plate = plateField.getText();
            int f,r,c;
            Vehicle v = new VehicleDAO().getVehicleByNumberPlate(plate);

            if (v != null) {
                String s = new ParkingSpaceDAO().assignParkingSpace(v);
                boolean b = new VehicleDAO().checkIfCheckedOut(v);
                if (b) {
                    if (s != null) {
                        String[] info = s.split(",");
                        f = Integer.parseInt(info[0]);
                        r = Integer.parseInt(info[1]);
                        c = Integer.parseInt(info[2]);
                        statusLabel.setText("Check-in successful for " + plate + "at " + "Floor :" + f + ", Row: " + r + ", Column: " + c);
                        statusLabel.setTextFill(Color.GREEN);
                        Transaction t = new Transaction(
                                0,
                                v,
                                new Pricing(v.getCategory(), v.getType()),
                                new ParkingSpaceDAO().getParkingSpaceByRowColFloor(r, c, f),
                                LocalDateTime.now(),
                                Integer.parseInt(driverIdField.getText())
                        );
                        int id = new TransactionDAO().saveNewTransaction(t);
                        t.setTransactionID(id);
                    }
                } else {
                    new AlertUser().showAlert(Alert.AlertType.WARNING,"Error!", "Vehicle is not checked out yet!");
                }
            }else {
                new AlertUser().showAlert(Alert.AlertType.WARNING, "Not Found",
                        "Vehicle with plate [" + plate + "] is not registered in the system.");
                statusLabel.setText("Check-in failed: Unknown Vehicle");
                statusLabel.setTextFill(Color.RED);
            }
        });
        contentArea.getChildren().addAll(title, new Label("Scan or type plate number below:"), plateField, driverIdField, btnConfirmIn, statusLabel);
    }

    private void showCheckOutView() {
        contentArea.getChildren().clear();
        Label title = new Label("Check Out Vehicle");
        title.setFont(Font.font("Arial", 22));

        TextField plateField = new TextField();
        plateField.setPromptText("Enter Plate Number");
        plateField.setMaxWidth(300);

        TextArea receiptArea = new TextArea();
        receiptArea.setEditable(false);
        receiptArea.setPrefHeight(200);

        Button btnCalculate = new Button("Calculate Bill");
        btnCalculate.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white;");

        Label statusLabel = new Label();
        statusLabel.setFont(Font.font("Verdana", 14));

        btnCalculate.setOnAction(e -> {
            String plate = plateField.getText();
            Vehicle v = new VehicleDAO().getVehicleByNumberPlate(plate);
            int id,f,r,c,d;
            String checkIn = "";
            if (v != null) {
                    boolean b = new VehicleDAO().checkIfCheckedOut(v);
                    if(!b) {
                        String s = new TransactionDAO().getTransactionByVehiclePlate(v.getNumberPlate());
                        if(s!=null) {
                            String [] info = s.split(",");
                            id = Integer.parseInt(info[0]);
                            f = Integer.parseInt(info[1]);
                            r = Integer.parseInt(info[2]);
                            c = Integer.parseInt(info[3]);
                            checkIn = info[4];
                            d = Integer.parseInt(info[5]);
                            statusLabel.setText("Check-out successful for " + plate + "at " +"Floor :"+ f + ", Row: " + r + ", Column: "+c);
                            statusLabel.setTextFill(Color.GREEN);
                            Transaction t = new Transaction(
                                    id,
                                    v,
                                    new Pricing(v.getCategory(), v.getType()),
                                    new ParkingSpaceDAO().getParkingSpaceByRowColFloor(r,c,f),
                                    LocalDateTime.parse(checkIn),
                                    d
                            );
                            t.processCheckout();
                            new TransactionDAO().updateCheckOut(t);
                            receiptArea.setText("Vehicle Number Plate: "+plate+"\n"+t.displayBill());
                        }
                    }else{
                        new AlertUser().showAlert(Alert.AlertType.WARNING,"Error!", "Vehicle is already checked out!");
                    }
                }else {
                new AlertUser().showAlert(Alert.AlertType.WARNING, "Not Found",
                        "Vehicle with plate [" + plate + "] is not registered in the system.");
                statusLabel.setText("Check-in failed: Unknown Vehicle");
                statusLabel.setTextFill(Color.RED);
            }
        });

        contentArea.getChildren().addAll(title, plateField, btnCalculate, statusLabel,receiptArea);
    }

    private void showHistoryView(String titleText, String prompt) {
        contentArea.getChildren().clear();
        Label title = new Label(titleText);
        title.setFont(Font.font("Arial", 22));

        HBox searchBox = new HBox(10, new TextField(prompt), new Button("Search"));
        searchBox.setAlignment(Pos.CENTER);

        TableView<Object> table = new TableView<>();
        TableColumn<Object, String> col1 = new TableColumn<>("Date");
        TableColumn<Object, String> col2 = new TableColumn<>("Plate");
        TableColumn<Object, String> col3 = new TableColumn<>("Amount");
        table.getColumns().addAll(col1, col2, col3);
        contentArea.getChildren().addAll(title, searchBox, table);
    }

    private Button createMenuButton(String text) {
        Button b = new Button(text);
        b.setPrefWidth(180);
        b.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-cursor: hand;");
        return b;
    }
}