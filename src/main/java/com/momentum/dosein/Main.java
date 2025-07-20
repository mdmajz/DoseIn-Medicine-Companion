package com.momentum.dosein;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the Dashboard FXML
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/momentum/dosein/views/dashboard.fxml")
        );

        // Create a fixed-size scene (600×400) and disable resizing
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("DoseIn");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
