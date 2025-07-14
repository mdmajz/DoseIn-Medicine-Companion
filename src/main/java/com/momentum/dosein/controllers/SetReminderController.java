package com.momentum.dosein.controllers;

import com.momentum.dosein.models.Reminder;
import com.momentum.dosein.utils.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SetReminderController {

    @FXML private TextField pillNameField;
    @FXML private VBox timeContainer;
    @FXML private TextArea infoArea;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("hh:mm a");

    @FXML
    public void initialize() {
        // seed with one row at current time
        addTimeRow(LocalTime.now().format(fmt));
    }

    /** Add one HBox: [TextField][➖][➕] */
    private void addTimeRow(String initial) {
        TextField tf = new TextField(initial);
        tf.setPrefWidth(200);
        tf.setPromptText("hh:mm a");

        Button btnMinus = new Button("➖");
        btnMinus.setOnAction(this::handleMinus);
        styleCircle(btnMinus);

        Button btnPlus = new Button("➕");
        btnPlus.setOnAction(this::handlePlus);
        styleCircle(btnPlus);

        HBox row = new HBox(10, tf, btnMinus, btnPlus);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
                "-fx-background-color: #084fc7; " +
                        "-fx-background-radius: 10; -fx-padding: 10;"
        );

        timeContainer.getChildren().add(row);
    }

    /** Circle-style for icon buttons */
    private void styleCircle(Button b) {
        b.setStyle(
                "-fx-text-fill: white; " +
                        "-fx-font-size: 18px; " +
                        "-fx-background-color: transparent; " +
                        "-fx-border-color: white; " +
                        "-fx-border-radius: 50%;"
        );
        b.setPrefSize(30, 30);
    }

    /** Insert a new row below the clicked one */
    @FXML private void handlePlus(ActionEvent e) {
        HBox thisRow = (HBox)((Node)e.getSource()).getParent();
        int idx = timeContainer.getChildren().indexOf(thisRow);
        addTimeRow(LocalTime.now().format(fmt));
        // move the newly added row right after current
        int last = timeContainer.getChildren().size() - 1;
        Node newRow = timeContainer.getChildren().remove(last);
        timeContainer.getChildren().add(idx + 1, newRow);
    }

    /** Remove row, but leave at least one */
    @FXML private void handleMinus(ActionEvent e) {
        if (timeContainer.getChildren().size() <= 1) {
            new Alert(Alert.AlertType.WARNING,
                    "At least one time is required.").showAndWait();
            return;
        }
        HBox thisRow = (HBox)((Node)e.getSource()).getParent();
        timeContainer.getChildren().remove(thisRow);
    }

    /** Save one Reminder per time-row */
    @FXML private void handleAdd(ActionEvent e) {
        String name = pillNameField.getText().trim();
        if (name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Pill name is required.").showAndWait();
            return;
        }

        List<Reminder> toSave = new ArrayList<>();
        for (Node node : timeContainer.getChildren()) {
            HBox row = (HBox) node;
            TextField tf = (TextField) row.getChildren().get(0);
            String timeTxt = tf.getText().trim();
            if (timeTxt.isEmpty()) continue;
            try {
                LocalTime t = LocalTime.parse(timeTxt, fmt);
                toSave.add(new Reminder(t, name, infoArea.getText().trim()));
            } catch (Exception ex) {
                new Alert(Alert.AlertType.WARNING,
                        "Invalid time format in one of the rows.").showAndWait();
                return;
            }
        }

        try {
            List<Reminder> all = FileManager.loadReminders();
            all.addAll(toSave);
            FileManager.saveReminders(all);
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR,
                    "Could not save.").showAndWait();
            return;
        }

        // go back to dashboard
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        try {
            Parent dash = FXMLLoader.load(
                    getClass().getResource("/com/momentum/dosein/views/dashboard.fxml")
            );
            Scene s = new Scene(dash);
            s.getStylesheets().add(
                    getClass().getResource("/com/momentum/dosein/css/style.css")
                            .toExternalForm());
            stage.setScene(s);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    @FXML private void handleCancel(ActionEvent e) {
        // identical back‐to‐dashboard logic
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        try {
            Parent dash = FXMLLoader.load(
                    getClass().getResource("/com/momentum/dosein/views/dashboard.fxml")
            );
            Scene s = new Scene(dash);
            s.getStylesheets().add(
                    getClass().getResource("/com/momentum/dosein/css/style.css")
                            .toExternalForm());
            stage.setScene(s);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
