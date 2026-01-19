package application;

import guis.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;
public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginView loginView = new LoginView();
        primaryStage.setTitle("Smart Parking - Secure Login");
        primaryStage.setScene(loginView.createLoginScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
