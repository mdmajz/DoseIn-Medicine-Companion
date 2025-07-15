package com.momentum.dosein.controllers;

import com.momentum.dosein.models.Reminder;
import com.momentum.dosein.utils.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SetReminderController {

    @FXML private TextField pillNameField;
    @FXML private HBox timeContainer;
    @FXML private TextField infoArea;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("hh:mm a");

    @FXML
    public void initialize() {
        addTimeTag(LocalTime.now().format(fmt));
    }

    private void addTimeTag(String time) {
        Button tag = new Button(time);
        tag.getStyleClass().add("time-tag");
        timeContainer.getChildren().add(tag);
    }

    @FXML
    private void handlePlus() {
        addTimeTag(LocalTime.now().format(fmt));
    }

    @FXML
    private void handleAdd() {
        String name = pillNameField.getText().trim();
        if (name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Pill name is required.").showAndWait();
            return;
        }

        List<Reminder> toSave = new ArrayList<>();
        for (var node : timeContainer.getChildren()) {
            if (node instanceof Button) {
                String timeTxt = ((Button) node).getText();
                try {
                    LocalTime t = LocalTime.parse(timeTxt, fmt);
                    toSave.add(new Reminder(t, name, infoArea.getText().trim()));
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.WARNING,
                            "Invalid time format on one of the tags.").showAndWait();
                    return;
                }
            }
        }

        try {
            List<Reminder> all = FileManager.loadReminders();
            all.addAll(toSave);
            FileManager.saveReminders(all);
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Could not save.").showAndWait();
            return;
        }

        // ↳ FIXED: cast Window → Stage directly
        Stage stage = (Stage) timeContainer.getScene().getWindow();
        loadDashboard(stage);
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) timeContainer.getScene().getWindow();
        loadDashboard(stage);
    }

    private void loadDashboard(Stage stage) {
        try {
            Parent dash = FXMLLoader.load(
                    getClass().getResource("/com/momentum/dosein/views/dashboard.fxml")
            );
            Scene s = new Scene(dash);
            s.getStylesheets().add(
                    getClass().getResource("/com/momentum/dosein/css/style.css").toExternalForm()
            );
            stage.setScene(s);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
