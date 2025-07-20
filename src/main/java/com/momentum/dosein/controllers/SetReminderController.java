package com.momentum.dosein.controllers;

import com.momentum.dosein.models.Reminder;
import com.momentum.dosein.utils.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SetReminderController implements Initializable {

    @FXML private TextField pillNameField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField additionalInfoField;

    @FXML private HBox timesContainer;
    @FXML private TextField hourField;
    @FXML private TextField minuteField;
    @FXML private ChoiceBox<String> amPmChoice;

    private final List<String> times = new ArrayList<>();
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("hh:mm a");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate AM/PM choices
        amPmChoice.getItems().addAll("AM", "PM");
        amPmChoice.setValue("AM");
    }

    @FXML
    private void handleAddTime() {
        String h = hourField.getText().trim();
        String m = minuteField.getText().trim();
        String ap = amPmChoice.getValue();
        if (h.isEmpty() || m.isEmpty()) {
            showError("Hour and minute are required");
            return;
        }
        try {
            int hi = Integer.parseInt(h);
            int mi = Integer.parseInt(m);
            if (hi < 1 || hi > 12 || mi < 0 || mi > 59) throw new NumberFormatException();
            LocalTime lt = LocalTime.of((hi % 12) + (ap.equals("PM") ? 12 : 0), mi);
            String formatted = lt.format(timeFmt);
            if (times.contains(formatted)) {
                showError("Time already added");
                return;
            }
            times.add(formatted);

            // Create a little card button
            Button b = new Button(formatted);
            b.getStyleClass().add("time-card");
            b.setOnAction(e -> {
                times.remove(formatted);
                timesContainer.getChildren().remove(b);
            });

            timesContainer.getChildren().add(b);
            hourField.clear();
            minuteField.clear();
        } catch (Exception ex) {
            showError("Invalid time");
        }
    }

    @FXML
    private void handleRemoveTime() {
        int count = times.size();
        if (count > 0) {
            times.remove(count - 1);
            timesContainer.getChildren().remove(count - 1);
        }
    }

    @FXML
    private void handleBack() {
        Stage st = (Stage) pillNameField.getScene().getWindow();
        try {
            Scene scene = new Scene(
                    FXMLLoader.load(getClass().getResource("/com/momentum/dosein/views/dashboard.fxml")),
                    600, 400
            );
            st.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSubmit() {
        String pill = pillNameField.getText().trim();
        LocalDate sd = startDatePicker.getValue();
        LocalDate ed = endDatePicker.getValue();
        String info = additionalInfoField.getText().trim();
        if (pill.isEmpty() || sd == null || ed == null || times.isEmpty()) {
            showError("Fill all fields and add at least one time");
            return;
        }

        // Save each time as an individual reminder entry
        for (String t : times) {
            String desc = pill + (info.isEmpty() ? "" : " – " + info);
            Reminder r = new Reminder(t, desc);
            FileManager.saveReminder(r);
        }

        Alert a = new Alert(Alert.AlertType.INFORMATION, "Reminder set successfully!");
        a.setHeaderText(null);
        a.showAndWait();
        handleBack();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
