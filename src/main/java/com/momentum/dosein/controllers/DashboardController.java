package com.momentum.dosein.controllers;

import com.momentum.dosein.models.Reminder;
import com.momentum.dosein.utils.FileManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML private Label timeLabel;
    @FXML private ListView<String> upcomingList;

    @FXML
    public void initialize() {
        // Live clock
        DateTimeFormatter dtfClock = DateTimeFormatter.ofPattern("hh:mm:ss a");
        Timeline clock = new Timeline(
                new KeyFrame(Duration.ZERO, e ->
                        timeLabel.setText(LocalTime.now().format(dtfClock))
                ),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        // Load & sort reminders
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("hh:mm a");
        List<Reminder> reminders = FileManager.loadReminders();
        reminders.sort(Comparator.comparing(r ->
                LocalTime.parse(r.getTime(), tf)
        ));

        // Map to display strings
        List<String> display = reminders.stream()
                .map(r -> {
                    String notes = r.getAdditional() != null && !r.getAdditional().isBlank()
                            ? " — " + r.getAdditional()
                            : "";
                    return String.format("%s — %s%s",
                            r.getTime(),
                            r.getMedicineName(),
                            notes
                    );
                })
                .collect(Collectors.toList());

        upcomingList.setItems(FXCollections.observableList(display));
    }

    @FXML private void handleSetReminder(ActionEvent e) {
        load(e, "/com/momentum/dosein/views/set_reminder.fxml", 600, 400);
    }
    @FXML private void handleManage(ActionEvent e) {
        load(e, "/com/momentum/dosein/views/manage_schedule.fxml", 600, 400);
    }
    @FXML private void handleDoctors(ActionEvent e) {
        load(e, "/com/momentum/dosein/views/doctor_contacts.fxml", 600, 400);
    }
    @FXML private void handleEmergency(ActionEvent e) {
        load(e, "/com/momentum/dosein/views/emergency.fxml", 600, 400);
    }
    @FXML private void handleAbout(ActionEvent e) {
        load(e, "/com/momentum/dosein/views/about_us.fxml", 600, 400);
    }
    @FXML private void handleSignOut(ActionEvent e) {
        load(e, "/com/momentum/dosein/views/login.fxml", 600, 400);
    }

    /**
     * Loads the given FXML, wraps it in a Scene (w×h), applies the stylesheet,
     * and swaps the current Stage’s scene.
     */
    private void load(ActionEvent e, String fxml, int w, int h) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Scene scene = new Scene(root, w, h);
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
