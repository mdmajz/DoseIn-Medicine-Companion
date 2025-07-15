package com.momentum.dosein;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Load the FXML
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/momentum/dosein/views/dashboard.fxml")
        );
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(
                getClass().getResource("/com/momentum/dosein/css/style.css")
                        .toExternalForm()
        );

        // Set window title and icon
        stage.setTitle("DoseIn : Complete Medicine Companion");
        Image icon = new Image(
                getClass().getResourceAsStream("/com/momentum/dosein/images/appicon.png")
        );
        stage.getIcons().add(icon);

        stage.setResizable(false);

        // Show the window
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
