package guis;

import DAOs.CustomerDAO;
import entities.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import logic.AlertUser;

public class Settings {
    User u;
    private final VBox contentArea;
    public Settings(VBox contentArea, User u){
        this.contentArea = contentArea;
        this.u = u;
    }
    public void showSettings() {
        contentArea.getChildren().clear();
        contentArea.setSpacing(30);
        contentArea.setAlignment(Pos.TOP_CENTER);

        // Header section
        Label title = new Label("Account Settings");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#2c3e50"));

        // Profile Card
        VBox profileCard = new VBox(20);
        profileCard.setPadding(new Insets(30));
        profileCard.setMaxWidth(500);
        profileCard.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        profileCard.getChildren().addAll(
                createSettingRow("Username", u.getUsername(), "username"),
                new Separator(),
                createSettingRow("Phone Number", u.getPhone(), "phone"),
                new Separator(),
                createSettingRow("Security", "••••••••", "password")
        );

        contentArea.getChildren().addAll(title, profileCard);
    }

    private HBox createSettingRow(String label, String value, String type) {
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
            if (type.equals("password")) handlePasswordChange();
            else handleUpdate(type, lblValue);
        });

        row.getChildren().addAll(textContainer, spacer, btnEdit);
        return row;
    }
    public void handlePasswordChange() {
        contentArea.getChildren().clear();

        VBox securityCard = new VBox(20);
        securityCard.setPadding(new Insets(30));
        securityCard.setMaxWidth(400);
        securityCard.setAlignment(Pos.CENTER);
        securityCard.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label title = new Label("Update Password");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 18));

        PasswordField pfCurrent = createStyledPasswordField("Current Password");
        PasswordField pfNew = createStyledPasswordField("New Password");
        PasswordField pfConfirm = createStyledPasswordField("Confirm New Password");

        Button btnUpdate = new Button("Update Security Settings");
        btnUpdate.setMaxWidth(Double.MAX_VALUE);
        btnUpdate.setPrefHeight(40);
        btnUpdate.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");

        Button btnCancel = new Button("Back to Settings");
        btnCancel.setStyle("-fx-background-color: transparent; -fx-text-fill: #7f8c8d;");
        btnCancel.setOnAction(e -> showSettings());

        btnUpdate.setOnAction(e -> {
            // Logic remains the same, but now inside this fancy UI
            if (!pfCurrent.getText().equals(u.getPassword())) {
                new AlertUser().showAlert(Alert.AlertType.ERROR, "Security Error", "Current password incorrect!");
            } else if (!pfNew.getText().equals(pfConfirm.getText()) || pfNew.getText().isEmpty()) {
                new AlertUser().showAlert(Alert.AlertType.ERROR, "Match Error", "Passwords do not match!");
            } else {
                boolean success = new DAOs.CustomerDAO().updateUser("password", pfNew.getText(), u.getUserID());
                if (success) {
                    u.setPassword(pfNew.getText());
                    new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated safely!");
                    showSettings();
                }
            }
        });

        securityCard.getChildren().addAll(title, pfCurrent, pfNew, pfConfirm, btnUpdate, btnCancel);
        contentArea.getChildren().add(securityCard);
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField pf = new PasswordField();
        pf.setPromptText(prompt);
        pf.setPrefHeight(40);
        pf.setStyle("-fx-background-radius: 5; -fx-border-color: #dcdde1; -fx-border-radius: 5;");
        return pf;
    }
    public void handleUpdate(String field, Label displayLabel){
        TextInputDialog dialog = new TextInputDialog(displayLabel.getText());
        dialog.setTitle("Update " + field);
        dialog.setHeaderText("Change your " + field);
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
//    public void handlePasswordChange(){
//        contentArea.getChildren().clear();
//        GridPane grid = new GridPane();
//        grid.setHgap(20);
//        grid.setVgap(20);
//        PasswordField pfCurrent = new PasswordField();
//        PasswordField pfNew = new PasswordField();
//        PasswordField pfNewConfirm = new PasswordField();
//        pfCurrent.setPromptText("Enter current password : ");
//        pfNew.setPromptText("Enter new password : ");
//        pfNewConfirm.setPromptText("Confirm new password : ");
//        Button btn = new Button("Change Password");
//        grid.add(pfCurrent, 0, 0);
//        grid.add(pfNew, 0, 1);
//        grid.add(pfNewConfirm, 0, 2);
//        grid.add(btn, 0, 3);
//        btn.setOnAction(e->{
//            boolean success = new CustomerDAO().updateUser("password", pfNew.getText(), u.getUserID());
//            boolean confirmedPassword = pfNew.getText().equals(pfNewConfirm.getText());
//            if(confirmedPassword) {
//                if (success) {
//                    new AlertUser().showAlert(Alert.AlertType.INFORMATION, "Success", "Password Update Successful!");
//                } else {
//                    new AlertUser().showAlert(Alert.AlertType.ERROR, "ERROR", "Password Update Error!");
//                }
//            }else{
//                new AlertUser().showAlert(Alert.AlertType.ERROR, "ERROR", "Passwords do not match! Try again!");
//            }
//        });
//        contentArea.getChildren().addAll(grid);
//    }
}
