package com.momentum.dosein.controllers;

import com.momentum.dosein.models.Reminder;
import com.momentum.dosein.utils.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SetReminderController {

    @FXML private TextField   pillNameField;
    @FXML private DatePicker  startDatePicker;
    @FXML private DatePicker  endDatePicker;
    @FXML private FlowPane    timeContainer;
    @FXML private TextField   infoArea;
    @FXML private Button      addTimeButton;
    @FXML private Button      removeTimeButton;

    @FXML
    public void initialize() {
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(LocalDate.now());
        addTimeSlot(LocalTime.now());
        updateAddButton();
    }

    private void addTimeSlot(LocalTime initial) {
        // Hour (1–12)
        TextField hour = new TextField(
                String.valueOf(initial.getHour() % 12 == 0 ? 12 : initial.getHour() % 12)
        );
        hour.setPromptText("HH");
        hour.getStyleClass().add("time-input");
        hour.setPrefWidth(60);

        // Minute (00–59)
        TextField minute = new TextField(
                String.format("%02d", initial.getMinute())
        );
        minute.setPromptText("MM");
        minute.getStyleClass().add("time-input");
        minute.setPrefWidth(60);

        // AM/PM
        ChoiceBox<String> ampm = new ChoiceBox<>(
                javafx.collections.FXCollections.observableArrayList("AM", "PM")
        );
        ampm.setValue(initial.getHour() >= 12 ? "PM" : "AM");
        ampm.getStyleClass().add("time-choice");
        ampm.setPrefWidth(80);

        HBox slot = new HBox(5, hour, new Label(":"), minute, ampm);
        slot.getStyleClass().add("time-slot");
        timeContainer.getChildren().add(slot);
    }

    @FXML private void handlePlus() {
        if (timeContainer.getChildren().size() < 3) {
            addTimeSlot(LocalTime.of(12, 0));
        }
        updateAddButton();
    }

    @FXML private void handleMinus() {
        int n = timeContainer.getChildren().size();
        if (n > 1) {
            timeContainer.getChildren().remove(n - 1);
        }
        updateAddButton();
    }

    private void updateAddButton() {
        addTimeButton.setDisable(timeContainer.getChildren().size() >= 3);
    }

    @FXML private void handleAdd() {
        String name = pillNameField.getText().trim();
        if (name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Pill name is required.").showAndWait();
            return;
        }
        if (startDatePicker.getValue().isAfter(endDatePicker.getValue())) {
            new Alert(Alert.AlertType.WARNING,
                    "Start date must be on or before End date").showAndWait();
            return;
        }

        List<Reminder> toSave = new ArrayList<>();
        for (var node : timeContainer.getChildren()) {
            HBox slot = (HBox) node;
            TextField hf = (TextField) slot.getChildren().get(0);
            TextField mf = (TextField) slot.getChildren().get(2);
            ChoiceBox<String> cb = (ChoiceBox<String>) slot.getChildren().get(3);

            int h, m;
            try {
                h = Integer.parseInt(hf.getText().trim());
                m = Integer.parseInt(mf.getText().trim());
                if (h < 1 || h > 12 || m < 0 || m > 59)
                    throw new NumberFormatException();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.WARNING,
                        "Invalid time: HH must be 1–12, MM 0–59.").showAndWait();
                return;
            }

            if ("PM".equals(cb.getValue()) && h < 12) h += 12;
            if ("AM".equals(cb.getValue()) && h == 12) h = 0;
            LocalTime t = LocalTime.of(h, m);
            toSave.add(new Reminder(t, name, infoArea.getText().trim()));
        }

        try {
            var all = FileManager.loadReminders();
            all.addAll(toSave);
            FileManager.saveReminders(all);
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Could not save reminders.").showAndWait();
            return;
        }
        backToDashboard();
    }

    @FXML private void handleCancel() {
        backToDashboard();
    }

    private void backToDashboard() {
        try {
            Stage st = (Stage) pillNameField.getScene().getWindow();
            Parent dash = FXMLLoader.load(
                    getClass().getResource("/com/momentum/dosein/views/dashboard.fxml")
            );
            Scene scene = new Scene(dash);
            scene.getStylesheets().add(
                    getClass().getResource("/com/momentum/dosein/css/style.css")
                            .toExternalForm()
            );
            st.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
