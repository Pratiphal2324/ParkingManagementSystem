package logic;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

public class AlertUser {
    public void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-font-family: 'Verdana';");
        alert.showAndWait();
    }
}
