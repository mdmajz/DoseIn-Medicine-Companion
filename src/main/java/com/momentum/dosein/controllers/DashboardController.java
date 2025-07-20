package com.momentum.dosein.controllers;

import com.momentum.dosein.models.Reminder;
import com.momentum.dosein.utils.FileManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label clockLabel;
    @FXML private VBox reminderContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1) Start live clock with seconds
        DateTimeFormatter clockFmt = DateTimeFormatter.ofPattern("hh:mm:ss a");
        Timeline clock = new Timeline(
                new KeyFrame(Duration.ZERO, e -> clockLabel.setText(LocalTime.now().format(clockFmt))),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();

        // 2) Load and display saved reminders
        List<Reminder> reminders = FileManager.loadReminders();
        for (Reminder r : reminders) {
            HBox row = new HBox(6);
            row.setPadding(new Insets(4));
            Label time = new Label(r.getTime());
            time.getStyleClass().add("time-label");
            Label desc = new Label(r.getDescription());
            desc.getStyleClass().add("desc-label");
            row.getChildren().addAll(time, desc);
            reminderContainer.getChildren().add(row);
        }
    }

    @FXML
    private void handleSettings(ActionEvent event) {
        showInfo("Settings clicked", "Settings");
    }

    @FXML
    private void handleSetReminder(ActionEvent event) {
        try {
            Stage st = (Stage)((Node)event.getSource()).getScene().getWindow();
            Parent rem = FXMLLoader.load(
                    getClass().getResource("/com/momentum/dosein/views/set_reminder.fxml")
            );
            st.setScene(new Scene(rem, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
            showError("Unable to open Set Reminder screen");
        }
    }

    @FXML
    private void handleManage(ActionEvent event) {
        showInfo("Manage clicked", "Manage Reminders");
    }

    @FXML
    private void handleDoctors(ActionEvent event) {
        showInfo(
                "Dr. Smith – Cardiologist\nDr. Emily – General Physician\nDr. John – Neurologist",
                "Doctors"
        );
    }

    @FXML
    private void handleEmergency(ActionEvent event) {
        showInfo(
                "🚨 999 (Ambulance)\n🚓 100 (Police)\n🏥 12345 (Nearest Hospital)",
                "Emergency Helpline"
        );
    }

    @FXML
    private void handleAbout(ActionEvent event) {
        showInfo("DoseIn v1.0\nDeveloped by Momentum Team\nYour health companion!", "About Us");
    }

    @FXML
    private void handleSignOut(ActionEvent event) {
        showInfo("Signed out", "Sign Out");
    }

    private void showInfo(String text, String title) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, text);
        a.setHeaderText(null);
        a.setTitle(title);
        a.showAndWait();
    }

    private void showError(String text) {
        Alert a = new Alert(Alert.AlertType.ERROR, text);
        a.setHeaderText(null);
        a.setTitle("Error");
        a.showAndWait();
    }
}
