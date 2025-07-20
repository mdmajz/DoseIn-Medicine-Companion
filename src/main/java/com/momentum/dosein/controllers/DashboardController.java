package com.momentum.dosein.controllers;

import com.momentum.dosein.models.Reminder;
import com.momentum.dosein.utils.FileManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public class DashboardController {
    @FXML private Label usernameLabel;
    @FXML private Label currentTime;
    @FXML private VBox reminderContainer;
    @FXML private Button profileButton;
    @FXML private Button historyButton;

    // include seconds
    private final DateTimeFormatter timeFmt =
            DateTimeFormatter.ofPattern("hh : mm : ss a");
    private final ObservableList<Reminder> masterReminders =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        usernameLabel.setText("User !");
        loadReminders();
        startClock();
    }

    private void startClock() {
        Timeline clock = new Timeline(
                new KeyFrame(Duration.ZERO, e -> {
                    LocalTime now = LocalTime.now();
                    currentTime.setText(now.format(timeFmt));
                    refreshReminders(now);
                }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    private void loadReminders() {
        try {
            List<Reminder> list = FileManager.loadReminders();
            masterReminders.setAll(list);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void refreshReminders(LocalTime now) {
        reminderContainer.getChildren().clear();
        masterReminders.stream()
                .filter(r -> !r.getTime().isBefore(now))
                .sorted(Comparator.comparing(Reminder::getTime))
                .forEach(r -> {
                    Label t = new Label(r.getTime().format(timeFmt));
                    t.getStyleClass().add("reminder-item-time");
                    String notes = (r.getNotes() == null || r.getNotes().isEmpty())
                            ? "" : " (" + r.getNotes() + ")";
                    Label d = new Label(r.getMedicine() + notes);
                    d.getStyleClass().add("reminder-item-desc");
                    VBox box = new VBox(5, t, d);
                    reminderContainer.getChildren().add(box);
                });
    }

    @FXML private void handleEditProfile(ActionEvent e) {
        System.out.println("Profile clicked");
    }
    @FXML private void handleHistory(ActionEvent e) {
        System.out.println("History clicked");
    }
    @FXML private void handleSetReminder(ActionEvent e) {
        switchScene(e,
                "/com/momentum/dosein/views/set_reminder.fxml",
                "DoseIn");
    }
    @FXML private void handleManage(ActionEvent e) {
        System.out.println("Manage clicked");
    }
    @FXML private void handleDoctors(ActionEvent e) {
        System.out.println("Doctors clicked");
    }
    @FXML private void handleEmergency(ActionEvent e) {
        System.out.println("Emergency clicked");
    }
    @FXML private void handleAboutUs(ActionEvent e) {
        System.out.println("About Us clicked");
    }
    @FXML private void handleSignOut(ActionEvent e) {
        System.out.println("Sign Out clicked");
    }

    /** Helper to swap scenes */
    private void switchScene(ActionEvent e, String fxmlPath, String title) {
        try {
            Stage stage = (Stage)((Button)e.getSource())
                    .getScene().getWindow();
            Parent root = FXMLLoader.load(
                    getClass().getResource(fxmlPath));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    getClass().getResource(
                            "/com/momentum/dosein/css/style.css"
                    ).toExternalForm());
            stage.setTitle(title);
            stage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
