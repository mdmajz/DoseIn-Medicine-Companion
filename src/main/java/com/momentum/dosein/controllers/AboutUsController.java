package com.momentum.dosein.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class AboutUsController {

    @FXML private ImageView teamImage;

    @FXML
    public void initialize() {
        // Load the team photo
        Image img = new Image(
                getClass().getResourceAsStream("/com/momentum/dosein/images/momentum.png")
        );
        teamImage.setImage(img);
    }

    @FXML
    private void handleGoBack(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/com/momentum/dosein/views/dashboard.fxml")
            );
            Scene scene = new Scene(root, 600, 400);
            scene.getStylesheets().add(
                    getClass()
                            .getResource("/com/momentum/dosein/css/style.css")
                            .toExternalForm()
            );
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
