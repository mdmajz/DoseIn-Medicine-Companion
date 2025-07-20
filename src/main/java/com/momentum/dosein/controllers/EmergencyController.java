package com.momentum.dosein.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EmergencyController {

    @FXML
    private void handleBack(ActionEvent evt) throws IOException {
        Stage stage = (Stage)((Node)evt.getSource()).getScene().getWindow();
        Parent dash = FXMLLoader.load(getClass()
                .getResource("/com/momentum/dosein/views/dashboard.fxml"));
        stage.setScene(new Scene(dash, 800, 500));
    }
}
