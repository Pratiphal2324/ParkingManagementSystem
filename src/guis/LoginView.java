package guis;

import DAOs.CustomerDAO;
import DAOs.VehicleDAO;
import entities.Customer;
import entities.Staff;
import entities.User;
import entities.Vehicle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import logic.AlertUser;
import logic.SignIn;

import java.util.ArrayList;
import java.util.Objects;

public class LoginView {
    VBox vehicleList;
    ScrollPane scrollPane;
    TextField usernameField;

    public Scene createLoginScene() {
        StackPane root = new StackPane();

        try {
            Image bgImg = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/parking_bg.jpg")));
            BackgroundImage bImg = new BackgroundImage(bgImg,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));

            Region backgroundRegion = new Region();
            backgroundRegion.setBackground(new Background(bImg));

            Region overlay = new Region();
            overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);"); // 40% black tint

            root.getChildren().addAll(backgroundRegion, overlay);
        } catch (Exception e) {
            System.out.println("Image not found. Falling back to gradient.");
            root.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #4ca1af);");
        }

        VBox loginCard = new VBox(20);
        loginCard.setMaxSize(350, 420);
        loginCard.setPadding(new Insets(40));
        loginCard.setAlignment(Pos.CENTER);

        loginCard.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); -fx-background-radius: 15;");

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        shadow.setRadius(30);
        loginCard.setEffect(shadow);

        Text title = new Text("SMART PARKING SYSTEM");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        title.setFill(Color.web("#2c3e50"));

        Text subtitle = new Text("Please login to your account");
        subtitle.setFill(Color.web("#7f8c8d"));

        usernameField = new TextField();
        usernameField.setPromptText("Username");
        styleInput(usernameField);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleInput(passwordField);

        vehicleList = new VBox(10);

        vehicleList.setAlignment(Pos.CENTER);
        Button loginBtn = new Button("LOGIN");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setPrefHeight(45);
        loginBtn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 8;");

        Label chooseVehicleLabel = new Label("Choose Your Vehicle:");
        chooseVehicleLabel.setVisible(false);

        scrollPane = new ScrollPane(vehicleList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(150);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        scrollPane.setVisible(false);
        scrollPane.setManaged(false);

        loginBtn.setOnMouseClicked(_ -> {
            User u = new SignIn().validateLogin(usernameField.getText(),passwordField.getText());
            if(u instanceof Customer) {
                Customer c = new CustomerDAO().getCustomerByUsernamePassword(usernameField.getText(),passwordField.getText());
                if(c==null){
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "", "Error!");
                }else {
                    boolean isEmpty = loadUserVehicles(c);
                    Hyperlink vehicleRegister = new Hyperlink("Register new vehicle? Click here");
                    vehicleRegister.setStyle("-fx-text-fill: #2980b9; -fx-underline: false; -fx-font-weight: bold;");
                    vehicleRegister.setOnMouseClicked(_ -> showVehicleRegistration(loginCard, new CustomerDAO().getCustomerByUserId(u.getUserID())));
                    vehicleList.getChildren().add(vehicleRegister);
                    if (isEmpty) {
                        loginBtn.setText("Register New Vehicle");
                        showVehicleRegistration(loginCard, new CustomerDAO().getCustomerByUserId(u.getUserID()));
                    } else {
                        scrollPane.setVisible(true);
                        scrollPane.setManaged(true);
                        chooseVehicleLabel.setVisible(true);
                    }
                }
            }
            else if(u instanceof Staff){
                if(((Staff) u).getJobTitle().equals("Manager")){
                    Stage stage = (Stage) loginBtn.getScene().getWindow();
                    stage.close();
                    new ManagerDashboardWindow(u).show(stage);
                }
                else if(((Staff) u).getJobTitle().equals("Accountant")) {
                    Stage stage = (Stage) loginBtn.getScene().getWindow();
                    stage.close();
                    new StaffDashboardWindow(u).show(stage);
                }
                else{
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "Access Denied", "Only Accountants and Managers can log in!");
                }
            }
        });


        loginBtn.setOnMouseEntered(_ -> loginBtn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 8;"));
        loginBtn.setOnMouseExited(_ -> loginBtn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 8;"));

        Hyperlink signupLink = new Hyperlink("Don't have an account? Sign Up");
        signupLink.setStyle("-fx-text-fill: #2980b9; -fx-underline: false; -fx-font-weight: bold;");
        signupLink.setOnMouseClicked(_ ->{
            SignupView signupView = new SignupView();
            Scene signupScene = signupView.createSignupScene("Customer");

            Stage currentStage = (Stage) signupLink.getScene().getWindow();

            currentStage.setScene(signupScene);});

        loginCard.getChildren().addAll(title, subtitle, usernameField, passwordField,chooseVehicleLabel, scrollPane, loginBtn, signupLink);

        root.getChildren().add(loginCard);

        return new Scene(root, 900, 650);
    }
    public boolean loadUserVehicles(Customer c) {
        vehicleList.setAlignment(Pos.CENTER);
        vehicleList.getChildren().clear();
        ArrayList<String> list = new VehicleDAO().getVehiclePlateByUserId(c.getUserID());
        if(list.isEmpty()){
            return true;
        }
        for (String plate : list) {
            Button plateBtn = new Button("Vehicle: " + plate);
            plateBtn.setPrefWidth(250);
            plateBtn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white;");

            plateBtn.setOnAction(_ -> {
                Stage stage = (Stage) plateBtn.getScene().getWindow();
                stage.close();
                new CustomerDashboardWindow(c).show(stage);
            });
            vehicleList.getChildren().add(plateBtn);
        }
        return false;
    }
    public void showVehicleRegistration(VBox loginCard, Customer u) {

        loginCard.getChildren().clear();
        loginCard.setSpacing(15);
        loginCard.setAlignment(Pos.CENTER_LEFT);

        Label header = new Label("Vehicle Registration");
        header.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        header.setTextFill(Color.web("#2c3e50"));

        VBox plateBox = new VBox(5);
        Label lblPlate = new Label("Vehicle Number Plate:");
        TextField plateField = new TextField();
        plateField.setPromptText("Enter Plate Number");
        plateBox.getChildren().addAll(lblPlate, plateField);

        VBox catBox = new VBox(5);
        Label lblCategory = new Label("Vehicle Category:");
        ToggleGroup categoryGroup = new ToggleGroup();
        RadioButton rbElectric = new RadioButton("TwoWheeler");
        rbElectric.setToggleGroup(categoryGroup);
        RadioButton rbFuel = new RadioButton("FourWheeler");
        rbFuel.setToggleGroup(categoryGroup);
        rbFuel.setSelected(true); // Default
        HBox catOptions = new HBox(15, rbElectric, rbFuel);
        catBox.getChildren().addAll(lblCategory, catOptions);

        VBox typeBox = new VBox(5);
        Label lblType = new Label("Vehicle Type:");
        ToggleGroup typeGroup = new ToggleGroup();
        RadioButton rbTwo = new RadioButton("Electric");
        rbTwo.setToggleGroup(typeGroup);
        RadioButton rbFour = new RadioButton("Fuel");
        rbFour.setToggleGroup(typeGroup);
        rbFour.setSelected(true); // Default
        HBox typeOptions = new HBox(15, rbTwo, rbFour);
        typeBox.getChildren().addAll(lblType, typeOptions);

        Button btnSubmit = new Button("Complete Registration");
        btnSubmit.setMaxWidth(Double.MAX_VALUE);
        btnSubmit.setPrefHeight(40);
        btnSubmit.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        btnSubmit.setOnAction(_ -> {
            String plate = plateField.getText().trim();
            String selectedCat = ((RadioButton) categoryGroup.getSelectedToggle()).getText();
            String selectedType = ((RadioButton) typeGroup.getSelectedToggle()).getText();

            if (plate.isEmpty()) {
                plateField.setStyle("-fx-border-color: red;");
            } else {
                Vehicle v = new VehicleDAO().registerVehicle(plate, selectedCat, selectedType, u.getUserID());
                if (v == null) {
                    new AlertUser().showAlert(Alert.AlertType.ERROR, "Registration Error", "Vehicle Registration not successful! Please Try Again");
                    }
                else{
                    Stage stage = (Stage) plateField.getScene().getWindow();
                    stage.close();
                    new CustomerDashboardWindow(u).show(stage);
                }
            }
        });

        loginCard.getChildren().addAll(header, new Separator(), plateBox, catBox, typeBox, btnSubmit);
    }
    private void styleInput(Control input) {
        input.setPrefHeight(40);
        input.setStyle("-fx-background-color: #ffffff; -fx-border-color: #bdc3c7; " +
                "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 0 10 0 10;");
    }
}